package org.javacord.event.channel.server.voice.impl;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.user.User;
import org.javacord.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerVoiceChannelMemberJoinEvent}.
 */
public class ImplServerVoiceChannelMemberJoinEvent extends ImplServerVoiceChannelMemberEvent
        implements ServerVoiceChannelMemberJoinEvent {

    /**
     * The old channel of the event.
     */
    private final ServerVoiceChannel oldChannel;

    /**
     * Creates a new server voice channel member join event.
     *
     * @param user The user of the event.
     * @param newChannel The new channel of the event.
     * @param oldChannel The old channel of the event.
     */
    public ImplServerVoiceChannelMemberJoinEvent(
            User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel) {
        super(user, newChannel);
        this.oldChannel = oldChannel;
    }

    @Override
    public Optional<ServerVoiceChannel> getOldChannel() {
        return Optional.ofNullable(oldChannel);
    }

    @Override
    public boolean isMove() {
        return oldChannel != null;
    }
}
