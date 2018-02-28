
package de.btobastian.javacord.event.channel.server.voice;

import de.btobastian.javacord.entity.channel.ServerVoiceChannel;
import de.btobastian.javacord.entity.user.User;

import java.util.Optional;

/**
 * A server voice channel member leave event.
 */
public class ServerVoiceChannelMemberLeaveEvent extends ServerVoiceChannelMemberEvent {

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
    public ServerVoiceChannelMemberLeaveEvent(User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel) {
        super(user, oldChannel);
        this.newChannel = newChannel;
    }

    /**
     * Gets the new channel of the event.
     *
     * @return The new channel of the event.
     */
    public Optional<ServerVoiceChannel> getNewChannel() {
        return Optional.ofNullable(newChannel);
    }

    /**
     * Gets whether this event is part of a move.
     *
     * @return whether this event is part of a move.
     */
    public boolean isMove() {
        return newChannel != null;
    }
}
