package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeLastMessageIdEvent;

/**
 * The implementation of {@link ServerForumChannelChangeLastMessageIdEvent}.
 */
public class ServerForumChannelChangeLastMessageIdEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeLastMessageIdEvent {

    /**
     * The old last message id of the channel.
     */
    private final long oldLastMessageId;

    /**
     * The new last message id of the channel.
     */
    private final long newLastMessageId;

    /**
     * Creates a new server forum channel change last message id event.
     *
     * @param channel The channel of the event.
     * @param oldLastMessageId The old last message id of the channel.
     * @param newLastMessageId The new last message id of the channel.
     */
    public ServerForumChannelChangeLastMessageIdEventImpl(ServerForumChannel channel,
                                                          long oldLastMessageId, long newLastMessageId) {
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
