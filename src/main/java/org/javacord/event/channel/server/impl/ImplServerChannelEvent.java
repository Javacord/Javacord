package org.javacord.event.channel.server.impl;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.event.channel.server.ServerChannelEvent;
import org.javacord.event.server.impl.ImplServerEvent;

/**
 * The implementation of {@link ServerChannelEvent}.
 */
public abstract class ImplServerChannelEvent extends ImplServerEvent implements ServerChannelEvent {

    /**
     * The channel of the event.
     */
    private final ServerChannel channel;

    /**
     * Creates a new server channel event.
     *
     * @param channel The channel of the event.
     */
    public ImplServerChannelEvent(ServerChannel channel) {
        super(channel.getServer());
        this.channel = channel;
    }

    @Override
    public ServerChannel getChannel() {
        return channel;
    }

}
