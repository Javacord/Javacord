package de.btobastian.javacord.utils.audio;

import com.neovisionaries.ws.client.*;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.Payload;
import de.btobastian.javacord.utils.payload.VoiceDataIdentify;
import de.btobastian.javacord.utils.payload.VoiceHeartbeat;
import de.btobastian.javacord.utils.payload.VoiceSelectProtocol;
import de.btobastian.javacord.utils.payload.VoiceStateUpdate;
import org.apache.http.HttpHost;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

public class AudioSocket extends WebSocketAdapter {
	
	private static final Logger logger = LoggerUtil.getLogger(AudioSocket.class);
	
	private static Timer timer;
	private Runnable hRunnable;
	
	private WebSocket webSocket;
	private DatagramSocket udpSocket;
	private InetSocketAddress address;
	private HttpHost proxy;
	
	private boolean connected = false;
	private boolean ready = false;
	private boolean ended = false;
	private boolean reconnect = false;
	private String gateway;
	private String sessionID;
	private String token;
	private int ssrc;
	private int status = 0;
	private byte[] key;
	
	/*
	Status
	-1 - channel deleted
	0 - not connected
	1 - connected, waiting for ready
	2 - connected, waiting for udp
	3 - ready
	 */
	
	private ImplDiscordAPI api;
	public AudioConnection connection;
	private String serverID;
	
	public AudioSocket(ImplDiscordAPI api, String gateway, String sessionID, String token, String serverID) {
		this.api = api;
		this.gateway = gateway.startsWith("wss://") ? gateway : "wss://" + gateway;
		this.sessionID = sessionID;
		this.token = token;
		this.serverID = serverID;
		this.reconnect = api.isAutoReconnectEnabled();
		
		if (timer == null) {
			timer = new Timer();
		}
		
		if (sessionID == null || sessionID.isEmpty()) throw new IllegalArgumentException("Can't create socket. No session ID.");
		if (token == null || token.isEmpty()) throw new IllegalArgumentException("Can't create socket. No token.");
		
		WebSocketFactory factory = new WebSocketFactory();
		proxy = api.getAudioProxy();
		if (proxy != null) {
			ProxySettings settings = factory.getProxySettings();
			settings.setHost(proxy.getHostName());
			settings.setPort(proxy.getPort());
		}
		try {
			webSocket = factory.createSocket(this.gateway);
			webSocket.addListener(this);
			webSocket.connect();
		} catch (IOException | WebSocketException e) {
			logger.warn("An error occurred while connecting to audio websocket", e);
		}
	}
	
	public void send(Payload payload) {
		webSocket.sendText(payload.toJSONString());
	}
	
	@Override
	public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
		logger.debug("Sending identify packet.");
		send(new VoiceDataIdentify(serverID, api.getYourself().getId(), sessionID, token));
		connected = true;
		status = 1;
	}
	
	@Override
	public void onTextMessage(WebSocket websocket, String text) throws Exception {
		JSONObject packet = new JSONObject(text);
		int op = packet.getInt("op");
		JSONObject data;
		
		logger.debug("Received Audio JSON (op: {})", op);
		logger.debug(packet.toString());
		
		switch (op) {
			case 2: // Ready
				data = packet.getJSONObject("d");
				ssrc = data.getInt("ssrc");
				String ip = data.getString("ip");
				int port = data.getInt("port");
				int heartbeat = data.getInt("heartbeat_interval");
				
				udpSocket = new DatagramSocket();
				address = new InetSocketAddress(ip, port);
				
				status = 2;
				
				send(new VoiceSelectProtocol(address.getHostString(), address.getPort()));
				startHeartbeat(heartbeat);
				break;
			case 3: // Heartbeat
				break;
			case 4: // Session Description
				JSONArray array = packet.getJSONObject("d").getJSONArray("secret_key");
				key = new byte[32];
				for (int i = 0; i < array.length(); i++) {
					key[i] = (byte) array.getInt(i);
				}
				ready = true;
				status = 3;
				break;
			case 5: // Speaking
				break;
			case 8: // Heartbeat start
				break;
			default:
				logger.debug("Received unknown audio packet (op: {})", op);
				logger.debug(packet.toString());
				break;
		}
	}
	
	@Override
	public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
		logger.debug("Audio socket closed. (by server: {})", closedByServer);
		if (serverCloseFrame != null) {
			logger.debug("Reason: " + serverCloseFrame.getCloseReason());
			logger.debug("Code: " + serverCloseFrame.getCloseCode());
		}
		if (clientCloseFrame != null) {
			logger.debug("Client Reason: " + clientCloseFrame.getCloseReason());
			int code = clientCloseFrame.getCloseCode();
			logger.debug("Client Code: " + code);
			if (code != 1000) close(1);
		}
		else close(0);
	}
	
	@Override
	public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
		handleCallbackError(websocket, cause);
	}
	
	@Override
	public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
		logger.warn("An error occured in the audio socket.", cause);
	}
	
	public void close(int reason) {
		/*
		0 - not connected
		1 - lost connection
		2 - unable to resolve udp
		3 - audio region change
		4 - channel deleted
		5 - removed from guild
		6 - connection timeout
		  */
		if (ended) return;
		ready = false;
		connected = false;
		ended = true;
		if (reason != 3) {
			api.getSocketAdapter().send(VoiceStateUpdate.leaveVoice(serverID));
		}
		if (hRunnable != null) {
			timer.cancel();
			hRunnable = null;
			//pool.remove(hRunnable);
			hRunnable = null;
		}
		if (udpSocket != null) udpSocket.close();
		if (webSocket != null && webSocket.isOpen()) {
			webSocket.sendClose(1000);
		}
		
		Server server = api.getServerById(serverID);
		if (server != null) {
			ImplAudioManager manager = (ImplAudioManager) server.getAudioManager();
			VoiceChannel oldChannel = manager.getConnectedChannel();
			manager.setConnection(null);
			if (reason == 1 && server.getVoiceChannelById(connection.getChannel().getId()) == null) reason = 4;
			status = -1;
			
			if (reconnect && reason != 0 && !(reason >= 3 && reason <= 5)) {
				manager.setConnecting(oldChannel);
				api.getSocketAdapter().send(new VoiceStateUpdate(oldChannel, false, false));
			}
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	private void startHeartbeat(int interval) {
		hRunnable = () -> {
			if (webSocket.isOpen() && !udpSocket.isClosed()) {
				send(new VoiceHeartbeat());
			}
		};
		
		try {
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					hRunnable.run();
				}
			}, 0, interval);
		} catch (RejectedExecutionException e) {
			logger.warn("Error while scheduling heartbeat", e);
		}
	}
	
	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}
	
	public int getSSRC() {
		return ssrc;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}
	
	public byte[] getKey() {
		return Arrays.copyOf(key, key.length);
	}
}
