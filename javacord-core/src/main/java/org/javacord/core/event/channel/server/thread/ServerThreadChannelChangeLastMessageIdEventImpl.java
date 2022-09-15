package org.javacord.core.event.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeLastMessageIdEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerThreadChannelChangeLastMessageIdEvent}.
 */
public class ServerThreadChannelChangeLastMessageIdEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeLastMessageIdEvent {

    private final long oldLastMessageId;
    private final long newLastMessageId;

    /**
     * Creates a new server thread channel change last message id event.
     *
     * @param channel The channel of the event.
     * @param oldLastMessageId The old last message id of the thread.
     * @param newLastMessageId The new last message id of the thread.
     */
    public ServerThreadChannelChangeLastMessageIdEventImpl(ServerThreadChannelImpl channel, long oldLastMessageId,
                                                           long newLastMessageId) {
        super(channel);
        this.oldLastMessageId = oldLastMessageId;
        this.newLastMessageId = newLastMessageId;
    }

    @Override
    public long getOldLastMessageId() {
        return oldLastMessageId;
    }

    @Override
    public long getNewLastMessageId() {
        return newLastMessageId;
    }
}
