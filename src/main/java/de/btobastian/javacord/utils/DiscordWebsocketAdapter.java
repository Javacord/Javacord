package de.btobastian.javacord.utils;

import com.google.common.util.concurrent.SettableFuture;
import com.neovisionaries.ws.client.*;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.utils.handler.ReadyHandler;
import de.btobastian.javacord.utils.handler.ResumedHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelCreateHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelDeleteHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelUpdateHandler;
import de.btobastian.javacord.utils.handler.message.*;
import de.btobastian.javacord.utils.handler.server.*;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleCreateHandler;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleDeleteHandler;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleUpdateHandler;
import de.btobastian.javacord.utils.handler.user.PresenceUpdateHandler;
import de.btobastian.javacord.utils.handler.user.UserGuildSettingsUpdateHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The main websocket adapter.
 */
public class DiscordWebsocketAdapter extends WebSocketAdapter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordWebsocketAdapter.class);

    private final ImplDiscordAPI api;
    private final HashMap<String, PacketHandler> handlers = new HashMap<>();
    private final SettableFuture<Boolean> ready = SettableFuture.create();
    private final String gateway;

    private WebSocket websocket = null;

    private Timer heartbeatTimer = null;

    private int heartbeatInterval = -1;
    private int lastSeq = -1;
    private String sessionId = null;

    public DiscordWebsocketAdapter(ImplDiscordAPI api, String gateway) {
        this.api = api;
        this.gateway = gateway;

        registerHandlers();

        connect();
    }

    private void connect() {
        WebSocketFactory factory = new WebSocketFactory();
        try {
            factory.setSSLContext(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("An error occurred while setting ssl context", e);
        }
        try {
            websocket = factory.createSocket(gateway + "&v=5");
            websocket.addListener(this);
            websocket.connect();
        } catch (IOException | WebSocketException e) {
            logger.warn("An error occurred while connecting to websocket", e);
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        if (sessionId == null) {
            JSONObject identifyPacket = new JSONObject()
                    .put("op", 2)
                    .put("d", new JSONObject()
                            .put("token", api.getToken())
                            .put("properties", new JSONObject()
                                    .put("$os", System.getProperty("os.name"))
                                    .put("$browser", "Javacord")
                                    .put("$device", "Javacord")
                                    .put("$referrer", "")
                                    .put("$referring_domain", ""))
                            .put("compress", true)
                            .put("large_threshold", 250));
            logger.debug("Sending identify packet");
            websocket.sendText(identifyPacket.toString());
        } else {
            JSONObject resumePacket = new JSONObject()
                    .put("op", 6)
                    .put("d", new JSONObject()
                        .put("token", api.getToken())
                        .put("session_id", sessionId)
                        .put("seq", lastSeq));
            logger.debug("Sending resume packet");
            websocket.sendText(resumePacket.toString());
        }
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        if (closedByServer) {
            logger.info("Websocket closed with reason {} and code {} by server!",
                    serverCloseFrame != null ? serverCloseFrame.getCloseReason() : "unknown",
                    serverCloseFrame != null ? serverCloseFrame.getCloseCode() : "unknown");
        } else {
            if (clientCloseFrame != null && clientCloseFrame.getCloseCode() == 1002
                    && "No more WebSocket frame from the server.".equals(clientCloseFrame.getCloseReason())) {
                logger.debug("Websocket closed! Trying to resume connection.");
            } else {
                logger.info("Websocket closed with reason {} and code {} by client!",
                        clientCloseFrame != null ? clientCloseFrame.getCloseReason() : "unknown",
                        clientCloseFrame != null ? clientCloseFrame.getCloseCode() : "unknown");
            }
        }

        if (!ready.isDone()) {
            ready.set(false);
            return;
        }
        // Reconnect
        if (heartbeatTimer != null) {
            heartbeatTimer.cancel();
            heartbeatTimer = null;
        }

        connect();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        JSONObject packet = new JSONObject(text);

        int op = packet.getInt("op");

        switch (op) {
            case 0:
                lastSeq = packet.getInt("s");
                String type = packet.getString("t");
                PacketHandler handler = handlers.get(type);
                if (handler != null) {
                    handler.handlePacket(packet.getJSONObject("d"));
                } else {
                    logger.debug("Received unknown packet of type {} (packet: {})", type, packet.toString());
                }

                if (type.equals("RESUMED")) {
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                    logger.debug("Received RESUMED packet");
                }
                if (type.equals("READY")) {
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                    sessionId = packet.getJSONObject("d").getString("session_id");
                    if (api.isWaitingForServersOnStartup()) {
                        // Discord sends us GUILD_CREATE packets after logging in. We will wait for them.
                        api.getThreadPool().getExecutorService().submit(new Runnable() {
                            @Override
                            public void run() {
                                int amount = api.getServers().size();
                                for (;;) {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException ignored) { }
                                    if (api.getServers().size() <= amount) {
                                        break; // two seconds without new servers becoming available
                                    }
                                    amount = api.getServers().size();
                                }
                                ready.set(true);
                            }
                        });
                    } else {
                        ready.set(true);
                    }
                    logger.debug("Received READY packet");
                }
                break;
            case 1:
                sendHeartbeat(websocket);
                break;
            case 7:
                logger.debug("Received op 7 packet. Reconnecting...");
                websocket.sendClose(1000);
                connect();
                break;
            case 9:
                // Invalid session :(
                break;
            case 10:
                JSONObject data = packet.getJSONObject("d");
                heartbeatInterval = data.getInt("heartbeat_interval");
                if (sessionId != null) {
                    // We are resuming and don't receive a READY packet.
                    heartbeatTimer = startHeartbeat(websocket, heartbeatInterval);
                }
                logger.debug("Received HELLO packet");
                break;
            case 11:
                // Heartbeat Ack. We don't really care about this packet
                break;
            default:
                logger.debug("Received unknown packet (op: {}, content: {})", op, packet.toString());
                break;
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        Inflater decompressor = new Inflater();
        decompressor.setInput(binary);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(binary.length);
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            int count;
            try {
                count = decompressor.inflate(buf);
            } catch (DataFormatException e) {
                logger.warn("An error occurred while decompressing data", e);
                return;
            }
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException ignored) { }
        byte[] decompressedData = bos.toByteArray();
        try {
            onTextMessage(websocket, new String(decompressedData, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.warn("An error occurred while decompressing data", e);
        }
    }

    /**
     * Starts the heartbeat.
     *
     * @param websocket The websocket the heartbeat should be sent to.
     * @param heartbeatInterval The heartbeat interval.
     * @return The timer used for the heartbeat.
     */
    private Timer startHeartbeat(final WebSocket websocket, final int heartbeatInterval) {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendHeartbeat(websocket);
                logger.debug("Sent heartbeat (interval: {})", heartbeatInterval);
            }
        }, 0, heartbeatInterval);
        return timer;
    }

    /**
     * Sends the heartbeat.
     *
     * @param websocket The websocket the heartbeat should be sent to.
     */
    private void sendHeartbeat(WebSocket websocket) {
        JSONObject heartbeatPacket = new JSONObject();
        heartbeatPacket.put("op", 1);
        heartbeatPacket.put("d", lastSeq);
        websocket.sendText(heartbeatPacket.toString());
    }

    /**
     * Registers all handlers.
     */
    private void registerHandlers() {
        // general
        addHandler(new ReadyHandler(api));
        addHandler(new ResumedHandler(api));

        // channel
        addHandler(new ChannelCreateHandler(api));
        addHandler(new ChannelDeleteHandler(api));
        addHandler(new ChannelUpdateHandler(api));

        // message
        addHandler(new MessageAckHandler(api));
        addHandler(new MessageCreateHandler(api));
        addHandler(new MessageDeleteHandler(api));
        addHandler(new MessageUpdateHandler(api));
        addHandler(new TypingStartHandler(api));

        // server
        addHandler(new GuildBanAddHandler(api));
        addHandler(new GuildBanRemoveHandler(api));
        addHandler(new GuildCreateHandler(api));
        addHandler(new GuildDeleteHandler(api));
        addHandler(new GuildMemberAddHandler(api));
        addHandler(new GuildMemberRemoveHandler(api));
        addHandler(new GuildMemberUpdateHandler(api));
        addHandler(new GuildUpdateHandler(api));

        // role
        addHandler(new GuildRoleCreateHandler(api));
        addHandler(new GuildRoleDeleteHandler(api));
        addHandler(new GuildRoleUpdateHandler(api));

        // user
        addHandler(new PresenceUpdateHandler(api));
        addHandler(new UserGuildSettingsUpdateHandler(api));
    }

    /**
     * Adds a packet handler.
     *
     * @param handler The handler to add.
     */
    private void addHandler(PacketHandler handler) {
        handlers.put(handler.getType(), handler);
    }

    /**
     * Gets the websocket of the adapter.
     *
     * @return The websocket of the adapter.
     */
    public WebSocket getWebSocket() {
        return websocket;
    }

    /**
     * Gets the Future which tells whether the connection is ready or failed.
     *
     * @return The Future.
     */
    public Future<Boolean> isReady() {
        return ready;
    }

    /**
     * Sends the update status packet
     */
    public void updateStatus() {
        logger.debug("Updating status (game: {}, idle: {})", api.getGame() == null ? "none" : api.getGame(), api.isIdle());
        JSONObject game = new JSONObject();
        game.put("name", api.getGame() == null ? JSONObject.NULL : api.getGame());
        if (api.getStreamingUrl() != null) {
            game.put("url", api.getStreamingUrl()).put("type", 1);
        }
        JSONObject updateStatus = new JSONObject()
                .put("op", 3)
                .put("d", new JSONObject()
                        .put("game", game)
                        .put("idle_since", api.isIdle() ? 1 : JSONObject.NULL));
        websocket.sendText(updateStatus.toString());
    }

    /* === ERROR LOGGING === */

    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        logger.warn("Websocket frame error!", cause);
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        if (!cause.getMessage().equals("Flushing frames to the server failed: Connection closed by remote host")) {
            logger.warn("Websocket error!", cause);
        }
    }

    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
        logger.warn("Websocket onMessage error!", cause);
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        logger.warn("Websocket onTextMessage error!", cause);
    }

    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
        logger.warn("Websocket onMessageDecompression error!", cause);
    }

    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        if (!cause.getMessage().equals("Flushing frames to the server failed: Connection closed by remote host")) {
            logger.warn("Websocket onSend error!", cause);
        }
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        logger.warn("Websocket onUnexpected error!", cause);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        logger.warn("Websocket onConnect error!", exception);
    }
}
