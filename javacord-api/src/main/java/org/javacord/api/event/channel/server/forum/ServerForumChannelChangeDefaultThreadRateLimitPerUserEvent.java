package org.javacord.api.event.channel.server.forum;

public interface ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent extends ServerForumChannelEvent {
    /**
     * Gets the new default thread rate limit per user of the forum channel.
     *
     * @return The new default thread rate limit per user of the forum channel.
     */
    int getNewDefaultThreadRateLimitPerUser();

    /**
     * Gets the old default thread rate limit per user of the forum channel.
     *
     * @return The old default thread rate limit per user of the forum channel.
     */
    int getOldDefaultThreadRateLimitPerUser();
}
