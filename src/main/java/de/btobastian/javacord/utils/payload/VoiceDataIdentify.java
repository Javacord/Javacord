package de.btobastian.javacord.utils.payload;

import de.btobastian.javacord.utils.Payload;
import org.json.JSONObject;

public class VoiceDataIdentify implements Payload {
	
	private String serverID;
	private String userID;
	private String sessionID;
	private String token;
	
	public VoiceDataIdentify(String serverID, String userID, String sessionID, String token) {
		this.serverID = serverID;
		this.userID = userID;
		this.sessionID = sessionID;
		this.token = token;
	}
	
	@Override
	public String toJSONString() {
		return new JSONObject()
				.put("op", 0)
				.put("d", new JSONObject()
						.put("server_id", serverID)
						.put("user_id", userID)
						.put("session_id", sessionID)
						.put("token", token))
				.toString();
	}
}
