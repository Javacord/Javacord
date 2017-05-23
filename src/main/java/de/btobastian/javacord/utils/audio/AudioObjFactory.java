package de.btobastian.javacord.utils.audio;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.utils.LoggerUtil;
import org.slf4j.Logger;

import java.util.HashMap;

public class AudioObjFactory {
	
	private static final Logger logger = LoggerUtil.getLogger(AudioObjFactory.class);
	
	private HashMap<String, ConnData> data = new HashMap<>();
	
	private ImplDiscordAPI api;
	
	public AudioObjFactory(ImplDiscordAPI api) {
		this.api = api;
	}
	
	private ConnData getData(String serverID) {
		return data.computeIfAbsent(serverID, k -> new ConnData(serverID));
	}
	
	private void tryOrFailCreate(ConnData data) {
		this.data.remove(data.getServerID());
		Server server = api.getServerById(data.getServerID());
		if (!data.isReady() || server == null) return;
		ImplAudioManager manager = (ImplAudioManager) server.getAudioManager();
		AudioSocket socket = new AudioSocket(api, data.getGateway(), data.getSessionID(), data.getToken(), data.getServerID());
		AudioConnection connection = new AudioConnection(socket, manager.getConnectingChannel());
		manager.setConnection(connection);
		logger.debug("Created AudioConnection (server: {})", data.getServerID());
	}
	
	public void voiceStateUpdate(String serverID, String sessionID) {
		if (api.getServerById(serverID) == null) {
			data.remove(serverID);
			return;
		}
		ConnData data = getData(serverID);
		logger.debug("Preparing session ID (server: {})", serverID);
		data.setSessionID(sessionID);
	}
	
	public void voiceServerUpdate(String serverID, String gateway, String token) {
		if (api.getServerById(serverID) == null) {
			data.remove(serverID);
			return;
		}
		ConnData data = getData(serverID);
		logger.debug("Preparing gateway and token (server: {})", serverID);
		data.setGateway(gateway);
		data.setToken(token);
		tryOrFailCreate(data);
	}
	
	private static class ConnData {
		private String gateway = null;
		private String serverID = null;
		private String sessionID = null;
		private String token = null;
		
		ConnData(String serverID) {
			this.serverID = serverID;
		}
		
		private boolean test(String value) {
			return value != null && !value.isEmpty();
		}
		
		public boolean isReady() {
			return test(gateway) && test(serverID) && test(sessionID) && test(token);
		}
		
		public String getGateway() {
			return gateway;
		}
		
		public void setGateway(String gateway) {
			this.gateway = gateway;
		}
		
		public String getServerID() {
			return serverID;
		}
		
		public String getSessionID() {
			return sessionID;
		}
		
		public void setSessionID(String sessionID) {
			this.sessionID = sessionID;
		}
		
		public String getToken() {
			return token;
		}
		
		public void setToken(String token) {
			this.token = token;
		}
	}
	
}
