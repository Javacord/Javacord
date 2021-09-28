package org.javacord.core.event.channel.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadCreateEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

public class ThreadCreateEventImpl extends ServerChannelEventImpl implements ThreadCreateEvent {

    /**
     * The channel of the event.
     */
    private final ServerThreadChannel serverThreadChannel;

    /**
     * Creates a new thread create event.
     *
     * @param serverThreadChannel The channel of the event.
     */
    public ThreadCreateEventImpl(final ServerThreadChannel serverThreadChannel) {
        super(serverThreadChannel);
        this.serverThreadChannel = serverThreadChannel;
    }

    @Override
    public ServerThreadChannel getChannel() {
        return serverThreadChannel;
    }

    @Override
    public Server getServer() {
        return serverThreadChannel.getServer();
    }
}
