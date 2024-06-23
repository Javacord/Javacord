package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeRateLimitPerUserEvent;

/**
 * The implementation of {@link ServerForumChannelChangeRateLimitPerUserEvent}.
 */
public class ServerForumChannelChangeRateLimitPerUserEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeRateLimitPerUserEvent {

    /**
     * The old rate limit per user of the channel.
     */
    private final int oldRateLimitPerUser;

    /**
     * The new rate limit per user of the channel.
     */
    private final int newRateLimitPerUser;

    /**
     * Creates a new server forum channel change rate limit per user event.
     *
     * @param channel The channel of the event.
     * @param oldRateLimitPerUser The old rate limit per user of the channel.
     * @param newRateLimitPerUser The new rate limit per user of the channel.
     */
    public ServerForumChannelChangeRateLimitPerUserEventImpl(final ServerForumChannel channel,
                                                             final int oldRateLimitPerUser,
                                                             final int newRateLimitPerUser) {
        super(channel);
        this.oldRateLimitPerUser = oldRateLimitPerUser;
        this.newRateLimitPerUser = newRateLimitPerUser;
    }

    @Override
    public int getOldRateLimitPerUser() {
        return oldRateLimitPerUser;
    }

    @Override
    public int getNewRateLimitPerUser() {
        return newRateLimitPerUser;
    }
}
