package de.btobastian.javacord.utils.payload;

import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.utils.Payload;
import org.json.JSONObject;

public class VoiceStateUpdate implements Payload {
	
	private String serverID;
	private String channelID;
	private boolean mute;
	private boolean deaf;
	
	public VoiceStateUpdate(String serverID, String channelID, boolean mute, boolean deaf) {
		this.serverID = serverID;
		this.channelID = channelID;
		this.mute = mute;
		this.deaf = deaf;
	}
	
	public VoiceStateUpdate(VoiceChannel channel, boolean mute, boolean deaf) {
		this(channel.getServer().getId(), channel.getId(), mute, deaf);
	}
	
	public static VoiceStateUpdate leaveVoice(String serverID) {
		return new VoiceStateUpdate(serverID, null, false, false);
	}
	
	@Override
	public String toJSONString() {
		return new JSONObject()
				.put("op", 4)
				.put("d", new JSONObject()
						.put("guild_id", serverID)
						.put("channel_id", channelID)
						.put("self_mute", mute)
						.put("self_deaf", deaf))
				.toString();
	}
}
