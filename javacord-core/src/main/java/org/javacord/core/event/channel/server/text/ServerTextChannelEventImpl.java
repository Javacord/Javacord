package org.javacord.core.event.channel.server.text;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.server.text.ServerTextChannelEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

/**
 * The implementation of {@link ServerTextChannelEvent}.
 */
public abstract class ServerTextChannelEventImpl extends ServerChannelEventImpl implements ServerTextChannelEvent {

    /**
     * The channel of the event.
     */
    private final ServerTextChannel channel;

    /**
     * Creates a new server text channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerTextChannelEventImpl(ServerTextChannel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public ServerTextChannel getChannel() {
        return channel;
    }

}
