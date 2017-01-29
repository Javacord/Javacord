package de.btobastian.javacord.utils.payload;

import de.btobastian.javacord.utils.Payload;
import org.json.JSONObject;

public class VoiceSelectProtocol implements Payload {
	
	private String protocol;
	private String address;
	private int port;
	private String mode;
	
	public VoiceSelectProtocol(String protocol, String address, int port, String mode) {
		this.protocol = protocol;
		this.address = address;
		this.port = port;
		this.mode = mode;
	}
	
	public VoiceSelectProtocol(String address, int port) {
		this("udp", address, port, "xsalsa20_poly1305");
	}
	
	@Override
	public String toJSONString() {
		return new JSONObject()
				.put("op", 1)
				.put("d", new JSONObject()
						.put("protocol", protocol)
						.put("data", new JSONObject()
								.put("address", address)
								.put("port", port)
								.put("mode", mode)))
				.toString();
	}
}
