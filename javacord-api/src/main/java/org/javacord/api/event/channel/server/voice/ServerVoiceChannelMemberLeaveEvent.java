package org.javacord.api.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.server.member.ServerMemberEvent;
import java.util.Optional;

/**
 * A server voice channel member leave event.
 */
public interface ServerVoiceChannelMemberLeaveEvent extends ServerVoiceChannelEvent, ServerMemberEvent {

    /**
     * Gets the new channel of the event.
     *
     * @return The new channel of the event.
     */
    Optional<ServerVoiceChannel> getNewChannel();

    /**
     * Gets whether this event is part of a move.
     *
     * @return whether this event is part of a move.
     */
    boolean isMove();

}
