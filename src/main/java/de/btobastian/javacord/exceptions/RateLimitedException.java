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

/**
 * This exception is always thrown if we receive a response which has rate_limit.
 */
public class RateLimitedException extends Exception {

    private final long retryAfter;
    private final long retryAt;

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     * @param retryAfter The "retry_after" received in the response.
     */
    public RateLimitedException(String message, long retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
        this.retryAt = System.currentTimeMillis() + retryAfter;
    }

    /**
     * Gets the "retry_after" received in the response.
     * Retry after is the time in milliseconds we have to wait for the next request.
     *
     * @return The "retry_after" received in the response.
     */
    public long getRetryAfter() {
        return retryAfter;
    }

    /**
     *  The calculated time when we can send a new messages.
     *
     * @return The calculated time when we can send a new message.
     */
    public long getRetryAt() {
        return retryAt;
    }

    /**
     * Causes the current thread to wait until we can retry the request.
     */
    public void waitTillRetry() throws InterruptedException {
        long time = getRetryAt() - System.currentTimeMillis();
        if (time < 1) {
            return;
        }
        Thread.sleep(time);
    }

}
