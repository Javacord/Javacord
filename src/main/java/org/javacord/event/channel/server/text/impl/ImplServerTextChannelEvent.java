package org.javacord.event.channel.server.text.impl;

import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.event.channel.server.impl.ImplServerChannelEvent;
import org.javacord.event.channel.server.text.ServerTextChannelEvent;

/**
 * The implementation of {@link ServerTextChannelEvent}.
 */
public abstract class ImplServerTextChannelEvent extends ImplServerChannelEvent implements ServerTextChannelEvent {

    /**
     * The channel of the event.
     */
    private final ServerTextChannel channel;

    /**
     * Creates a new server text channel event.
     *
     * @param channel The channel of the event.
     */
    public ImplServerTextChannelEvent(ServerTextChannel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public ServerTextChannel getChannel() {
        return channel;
    }

}
