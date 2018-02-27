package de.btobastian.javacord.event.channel.server;

import de.btobastian.javacord.entity.channel.ServerChannel;
import de.btobastian.javacord.event.server.ServerEvent;

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
