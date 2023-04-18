package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent;

/**
 * The implementation of {@link ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent}.
 */
public class ServerForumChannelChangeDefaultThreadRateLimitPerUserEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent {

    /**
     * The new default thread rate limit per user of the forum channel.
     */
    private final int newDefaultThreadRateLimitPerUser;

    /**
     * The old default thread rate limit per user of the forum channel.
     */
    private final int oldDefaultThreadRateLimitPerUser;

    /**
     * Creates a new server forum channel change default thread rate limit per user event.
     *
     * @param channel The channel of the event.
     * @param newDefaultThreadRateLimitPerUser The new default thread rate limit per user of the forum channel.
     * @param oldDefaultThreadRateLimitPerUser The old default thread rate limit per user of the forum channel.
     */
    public ServerForumChannelChangeDefaultThreadRateLimitPerUserEventImpl(
            ServerForumChannel channel, int newDefaultThreadRateLimitPerUser, int oldDefaultThreadRateLimitPerUser) {
        super(channel);
        this.newDefaultThreadRateLimitPerUser = newDefaultThreadRateLimitPerUser;
        this.oldDefaultThreadRateLimitPerUser = oldDefaultThreadRateLimitPerUser;
    }

    @Override
    public int getNewDefaultThreadRateLimitPerUser() {
        return newDefaultThreadRateLimitPerUser;
    }

    @Override
    public int getOldDefaultThreadRateLimitPerUser() {
        return oldDefaultThreadRateLimitPerUser;
    }
}
