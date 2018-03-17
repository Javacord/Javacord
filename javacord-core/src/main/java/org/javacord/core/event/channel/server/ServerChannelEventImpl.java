package org.javacord.core.event.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.ServerChannelEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerChannelEvent}.
 */
public abstract class ServerChannelEventImpl extends ServerEventImpl implements ServerChannelEvent {

    /**
     * The channel of the event.
     */
    private final ServerChannel channel;

    /**
     * Creates a new server channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerChannelEventImpl(ServerChannel channel) {
        super(channel.getServer());
        this.channel = channel;
    }

    @Override
    public ServerChannel getChannel() {
        return channel;
    }

}
