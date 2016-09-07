/*
 * Copyright (C) 2016 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.exceptions;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.utils.ratelimits.RateLimitManager;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;

/**
 * This exception is always thrown if we receive a response which has rate_limit.
 */
public class RateLimitedException extends Exception {

    private final long retryAfter;
    private final RateLimitType type;
    private final Server server;
    private final RateLimitManager manager;

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @param manager The rate limit manager.
     */
    public RateLimitedException(String message, long retryAfter, RateLimitType type, Server server, RateLimitManager manager) {
        super(message);
        this.retryAfter = retryAfter;
        this.type = type;
        this.server = server;
        this.manager = manager;
    }

    /**
     * Gets the type of the rate limit.
     *
     * @return The type of the rate limit.
     */
    public RateLimitType getType() {
        return type;
    }

    /**
     * Gets the server of the rate limit.
     *
     * @return The server of the rate limit. Can be <code>null</code> for non-server related limits.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the "retry_after" received in the response.
     * Retry after is the time in milliseconds we have to wait for the next request.
     * NOTE: The value does not get updated!
     *
     * @return The "retry_after" received in the response.
     */
    public long getRetryAfter() {
        return retryAfter;
    }

    /**
     * The calculated time when we can send a new messages.
     *
     * @return The calculated time when we can send a new message.
     */
    public long getRetryAt() {
        return System.currentTimeMillis() +  manager.getRateLimit(type, server);
    }

    /**
     * Causes the current thread to wait until we can retry the request.
     */
    public void waitTillRetry() throws InterruptedException {
        long time = manager.getRateLimit(type, server);
        if (time < 1) {
            return;
        }
        Thread.sleep(time);
    }

}
