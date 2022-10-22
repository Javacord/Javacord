package org.javacord.core.event.channel.server.message;

import org.javacord.api.entity.channel.ServerMessageChannel;
import org.javacord.api.event.channel.server.message.ServerMessageChannelEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

/**
 * The implementation of {@link ServerMessageChannelEvent}.
 */
public abstract class ServerMessageChannelEventImpl extends ServerChannelEventImpl
        implements ServerMessageChannelEvent {

    /**
     * The channel of the event.
     */
    private final ServerMessageChannel channel;

    /**
     * Creates a new server message channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerMessageChannelEventImpl(ServerMessageChannel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public ServerMessageChannel getChannel() {
        return channel;
    }
}
