package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.events.user.UserEvent;

/**
 * A server voice channel member event.
 */
public abstract class ServerVoiceChannelMemberEvent extends UserEvent {

    /**
     * The channel of the event.
     */
    private final ServerVoiceChannel channel;

    /**
     * Creates a new voice channel member event.
     *
     * @param user The user of the event.
     * @param channel The channel of the event.
     */
    public ServerVoiceChannelMemberEvent(User user, ServerVoiceChannel channel) {
        super(channel.getApi(), user);
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public ServerVoiceChannel getChannel() {
        return channel;
    }

}
