
package org.javacord.event.channel.server.voice.impl;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.user.User;
import org.javacord.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerVoiceChannelMemberLeaveEvent}.
 */
public class ImplServerVoiceChannelMemberLeaveEvent extends ImplServerVoiceChannelMemberEvent
        implements ServerVoiceChannelMemberLeaveEvent {

    /**
     * The new channel of the event.
     */
    private final ServerVoiceChannel newChannel;

    /**
     * Creates a new server voice channel member leave event.
     *
     * @param user The user of the event.
     * @param newChannel The new channel of the event.
     * @param oldChannel The old channel of the event.
     */
    public ImplServerVoiceChannelMemberLeaveEvent(
            User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel) {
        super(user, oldChannel);
        this.newChannel = newChannel;
    }

    @Override
    public Optional<ServerVoiceChannel> getNewChannel() {
        return Optional.ofNullable(newChannel);
    }

    @Override
    public boolean isMove() {
        return newChannel != null;
    }
}
