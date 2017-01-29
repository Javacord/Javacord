package de.btobastian.javacord.utils.payload;

import de.btobastian.javacord.utils.Payload;
import org.json.JSONObject;

public class VoiceSpeaking implements Payload {
	
	private boolean speaking;
	private int delay;
	
	public VoiceSpeaking(boolean speaking, int delay) {
		this.speaking = speaking;
		this.delay = delay;
	}
	
	public VoiceSpeaking(boolean speaking) {
		this(speaking, 0);
	}
	
	@Override
	public String toJSONString() {
		return new JSONObject()
				.put("op", 5)
				.put("d", new JSONObject()
						.put("speaking", speaking)
						.put("delay", delay))
				.toString();
	}
}
