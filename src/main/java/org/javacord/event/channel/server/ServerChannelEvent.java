package org.javacord.event.channel.server;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.event.server.ServerEvent;
import org.javacord.event.server.ServerEvent;

/**
 * A server channel event.
 */
public abstract class ServerChannelEvent extends ServerEvent {

    /**
     * The channel of the event.
     */
    private final ServerChannel channel;

    /**
     * Creates a new server channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerChannelEvent(ServerChannel channel) {
        super(channel.getApi(), channel.getServer());
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public ServerChannel getChannel() {
        return channel;
    }

}
