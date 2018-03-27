package org.javacord.api.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.user.UserEvent;

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
