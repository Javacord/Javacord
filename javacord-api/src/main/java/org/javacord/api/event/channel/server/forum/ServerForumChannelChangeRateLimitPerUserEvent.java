package org.javacord.api.event.channel.server.forum;

/**
 * A server forum channel change topic event.
 */
public interface ServerForumChannelChangeRateLimitPerUserEvent extends ServerForumChannelEvent {

    /**
     * Gets the new rate limit per user of the forum channel.
     *
     * @return The new rate limit per user of the forum channel.
     */
    int getNewRateLimitPerUser();

    /**
     * Gets the old rate limit per user of the forum channel.
     *
     * @return The old rate limit per user of the forum channel.
     */
    int getOldRateLimitPerUser();
}
