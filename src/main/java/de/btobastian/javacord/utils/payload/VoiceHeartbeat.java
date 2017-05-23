package de.btobastian.javacord.utils.payload;

import de.btobastian.javacord.utils.Payload;
import org.json.JSONObject;

public class VoiceHeartbeat implements Payload {
	
	private long time;
	
	public VoiceHeartbeat(long time) {
		if (time == -1) this.time = System.currentTimeMillis();
		else this.time = time;
	}
	
	public VoiceHeartbeat() {
		time = System.currentTimeMillis();
	}
	
	@Override
	public String toJSONString() {
		return new JSONObject()
				.put("op", 3)
				.put("d", time == -1 ? null : time)
				.toString();
	}
}
