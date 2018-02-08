package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.VoiceChannel;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.events.user.UserJoinVoiceChannelEvent;
import de.btobastian.javacord.events.user.UserLeaveVoiceChannelEvent;
import de.btobastian.javacord.utils.PacketHandler;

/**
 * Handles the voice state update packet.
 */
public class VoiceStateUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceStateUpdateHandler(DiscordApi api) {
        super(api, true, "VOICE_STATE_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        if (packet.hasNonNull("user_id")) {
            ImplUser user = (ImplUser) api.getCachedUserById(packet.get("user_id").asLong()).get();
            VoiceChannel oldChannel = null;
            if (user.getConnectedVoiceChannel().isPresent()) {
                oldChannel = user.getConnectedVoiceChannel().get();
            }
            if (packet.hasNonNull("channel_id")) {
                Long channelId = packet.get("channel_id").asLong();
                if (oldChannel != null) {
                    if (channelId == oldChannel.getId()) {
                        return;
                    }
                    oldChannel.removeConnectedUser(user);
                }
                final VoiceChannel newChannel = api.getVoiceChannelById(channelId).get();
                newChannel.addConnectedUser(user);
                user.setConnectedVoiceChannel(newChannel);
                dispatchUserJoinVoiceChannelEvent(user, newChannel, oldChannel);
            } else if (oldChannel != null) {
                oldChannel.removeConnectedUser(user);
                user.setConnectedVoiceChannel(null);
                dispatchUserLeaveVoiceChannelEvent(user, oldChannel);
            }
        }
    }

    public void dispatchUserJoinVoiceChannelEvent(ImplUser user, VoiceChannel newChannel, VoiceChannel oldChannel) {
        UserJoinVoiceChannelEvent event = new UserJoinVoiceChannelEvent(api, user, newChannel, oldChannel);
        dispatchEvent(api.getUserJoinVoiceChannelListeners(), listener -> listener.onUserJoinVoiceChannel(event));
    }

    public void dispatchUserLeaveVoiceChannelEvent(final ImplUser user, VoiceChannel oldChannel) {
        UserLeaveVoiceChannelEvent event = new UserLeaveVoiceChannelEvent(api, user, oldChannel);
        dispatchEvent(api.getUserLeaveVoiceChannelListeners(), listener -> listener.onUserLeaveVoiceChannel(event));
    }

}
