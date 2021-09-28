package org.javacord.core.event.channel.thread;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.thread.ThreadUpdateEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

public class ThreadUpdateEventImpl extends ServerChannelEventImpl implements ThreadUpdateEvent {

    /**
     * The channel of the event.
     */
    private final ServerThreadChannel serverThreadChannel;

    /**
     * Creates a new thread update event.
     *
     * @param serverThreadChannel The channel of the event.
     */
    public ThreadUpdateEventImpl(final ServerThreadChannel serverThreadChannel) {
        super(serverThreadChannel);
        this.serverThreadChannel = serverThreadChannel;
    }

    @Override
    public ServerThreadChannel getChannel() {
        return serverThreadChannel;
    }

    @Override
    public DiscordApi getApi() {
        return getChannel().getApi();
    }
}
