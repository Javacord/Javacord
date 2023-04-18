package org.javacord.api.event.channel.server.forum;

/**
 * An event signaling that a server forum channel's default thread rate limit per user has changed.
 */
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
