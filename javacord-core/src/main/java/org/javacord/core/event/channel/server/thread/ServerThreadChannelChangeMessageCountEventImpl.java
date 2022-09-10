package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMessageCountEvent;

/**
 * The implementation of {@link ServerThreadChannelChangeMessageCountEvent}.
 */
public class ServerThreadChannelChangeMessageCountEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeMessageCountEvent {
    /**
     * The new count of messages in this thread.
     */
    private final int newMessageCount;

    /**
     * The old count of messages in this thread.
     */
    private final int oldMessageCount;

    /**
     * Creates a new server text channel message count update event.
     *
     * @param channel The channel of the event.
     * @param newMessageCount The new count of messages in this thread.
     * @param oldMessageCount The old count of messages in this thread.
     */
    public ServerThreadChannelChangeMessageCountEventImpl(ServerThreadChannel channel,
                                                          int newMessageCount, int oldMessageCount) {
        super(channel);
        this.newMessageCount = newMessageCount;
        this.oldMessageCount = oldMessageCount;
    }

    @Override
    public int getNewMessageCount() {
        return newMessageCount;
    }

    @Override
    public int getOldMessageCount() {
        return oldMessageCount;
    }
}
