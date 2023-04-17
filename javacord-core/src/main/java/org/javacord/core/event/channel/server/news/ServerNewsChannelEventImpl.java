package org.javacord.core.event.channel.server.news;

import org.javacord.api.entity.channel.ServerNewsChannel;
import org.javacord.api.event.channel.server.news.ServerNewsChannelEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

/**
 * The implementation of {@link ServerNewsChannelEvent}.
 */
public class ServerNewsChannelEventImpl extends ServerChannelEventImpl implements ServerNewsChannelEvent {
    /**
     * The channel of the event.
     */
    private final ServerNewsChannel channel;

    /**
     * Creates a new server news channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerNewsChannelEventImpl(ServerNewsChannel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public ServerNewsChannel getChannel() {
        return channel;
    }
}
