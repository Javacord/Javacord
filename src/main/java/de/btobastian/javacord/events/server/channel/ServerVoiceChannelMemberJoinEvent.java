
package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;

import java.util.Optional;

/**
 * A server voice channel member join event.
 */
public class ServerVoiceChannelMemberJoinEvent extends ServerVoiceChannelMemberEvent {

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
    public ServerVoiceChannelMemberJoinEvent(User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel) {
        super(user, newChannel);
        this.oldChannel = oldChannel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public Optional<ServerVoiceChannel> getOldChannel() {
        return Optional.ofNullable(oldChannel);
    }

    /**
     * Gets whether this event is part of a move.
     *
     * @return whether this event is part of a move.
     */
    public boolean isMove() {
        return oldChannel != null;
    }
}
