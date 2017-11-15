package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A server text channel event.
 */
public abstract class ServerTextChannelEvent extends ServerEvent {

    /**
     * The channel of the event.
     */
    private final ServerTextChannel channel;

    /**
     * Creates a new server text channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerTextChannelEvent(ServerTextChannel channel) {
        super(channel.getApi(), channel.getServer());
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public ServerTextChannel getChannel() {
        return channel;
    }

}
