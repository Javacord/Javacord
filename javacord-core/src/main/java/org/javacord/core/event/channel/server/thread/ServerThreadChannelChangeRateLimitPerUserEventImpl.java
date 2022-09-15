package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeRateLimitPerUserEvent;

public class ServerThreadChannelChangeRateLimitPerUserEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeRateLimitPerUserEvent {

    /**
     * The new rate limit per user.
     */
    private final int newRateLimitPerUser;

    /**
     * The old rate limit per user.
     */
    private final int oldRateLimitPerUser;

    /**
     * Creates a new server thread channel change rate limit per user event.
     *
     * @param channel The channel of the event.
     * @param newRateLimitPerUser The new rate limit per user of the channel.
     * @param oldRateLimitPerUser The old rate limit per user of the channel.
     */
    public ServerThreadChannelChangeRateLimitPerUserEventImpl(ServerThreadChannel channel, int newRateLimitPerUser,
                                                              int oldRateLimitPerUser) {
        super(channel);
        this.newRateLimitPerUser = newRateLimitPerUser;
        this.oldRateLimitPerUser = oldRateLimitPerUser;
    }

    @Override
    public int getNewRateLimitPerUser() {
        return newRateLimitPerUser;
    }

    @Override
    public int getOldRateLimitPerUser() {
        return oldRateLimitPerUser;
    }
}
