package org.javacord.event.channel.server.voice;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.event.user.UserEvent;

import java.util.Optional;

/**
 * A server voice channel member join event.
 */
public interface ServerVoiceChannelMemberJoinEvent extends ServerVoiceChannelEvent, UserEvent {

    /**
     * Gets the old channel of the event.
     *
     * @return The old channel of the event.
     */
    Optional<ServerVoiceChannel> getOldChannel();

    /**
     * Gets whether this event is part of a move.
     *
     * @return whether this event is part of a move.
     */
    boolean isMove();

}
