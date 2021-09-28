package org.javacord.core.event.channel.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadDeleteEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

public class ThreadDeleteEventImpl extends ServerChannelEventImpl implements ThreadDeleteEvent {

    /**
     * The channel of the event.
     */
    private final ServerThreadChannel serverThreadChannel;

    /**
     * Creates a new thread delete event.
     *
     * @param channel The channel of the event.
     */
    public ThreadDeleteEventImpl(final ServerThreadChannel channel) {
        super(channel);
        this.serverThreadChannel = channel;
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
