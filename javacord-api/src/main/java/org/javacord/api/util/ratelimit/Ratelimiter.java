package org.javacord.api.util.ratelimit;

/**
 * Can be used to implement ratelimits.
 */
public interface Ratelimiter {

    /**
     * Blocks the requesting thread until a quota becomes available.
     *
     * @throws InterruptedException if any thread has interrupted the current thread.
     *                              The interrupted status of the current thread is cleared when this exception is
     *                              thrown.
     */
    void requestQuota() throws InterruptedException;

}
