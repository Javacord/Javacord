package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeRateLimitPerUserEvent extends ServerThreadChannelEvent {
    /**
     * Gets the old rate limit per user.
     *
     * @return The old rate limit per user.
     */
    int getOldRateLimitPerUser();

    /**
     * Gets the new rate limit per user.
     *
     * @return The new rate limit per user.
     */
    int getNewRateLimitPerUser();
}
