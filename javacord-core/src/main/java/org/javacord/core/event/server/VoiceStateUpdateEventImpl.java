package org.javacord.core.event.server;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.server.VoiceStateUpdateEvent;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelEventImpl;

public class VoiceStateUpdateEventImpl extends ServerVoiceChannelEventImpl implements VoiceStateUpdateEvent {

    private final String sessionId;

    /**
     * Constructs a new VoiceStateUpdateEventImpl instance.
     *
     * @param channel   The channel this voice server update is for.
     * @param sessionId The session id for this user.
     */
    public VoiceStateUpdateEventImpl(ServerVoiceChannel channel, String sessionId) {
        super(channel);
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

}
