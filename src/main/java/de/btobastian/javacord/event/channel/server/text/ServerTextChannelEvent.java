package de.btobastian.javacord.event.channel.server.text;

import de.btobastian.javacord.entity.channel.ServerTextChannel;
import de.btobastian.javacord.event.server.ServerEvent;

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
