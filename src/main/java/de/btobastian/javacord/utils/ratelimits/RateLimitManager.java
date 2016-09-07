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
package de.btobastian.javacord.utils.ratelimits;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.utils.LoggerUtil;
import org.slf4j.Logger;

import java.util.HashMap;

/**
 * This class manages rate limits.
 */
public class RateLimitManager {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(RateLimitManager.class);

    // all non-server related limits (e.g. username updates)
    private final HashMap<RateLimitType, Long> rateLimits = new HashMap<>();
    // all server related limits (e.g. sending messages)
    private final HashMap<Server, HashMap<RateLimitType, Long>> serverRateLimits = new HashMap<>();

    /**
     * Adds a rate limit for the given type.
     *
     * @param type The type of the rate limit.
     * @param retryAfter The
     */
    public void addRateLimit(RateLimitType type, long retryAfter) {
        addRateLimit(type, null, retryAfter);
    }

    /**
     * Adds a rate limit for the given type.
     *
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @param retryAfter The
     */
    public void addRateLimit(RateLimitType type, Server server, long retryAfter) {
        if (server == null) {
            rateLimits.put(type, System.currentTimeMillis() + retryAfter);
        } else {
            HashMap<RateLimitType, Long> rateLimits = serverRateLimits.get(server);
            if (rateLimits == null) {
                rateLimits = new HashMap<>();
                serverRateLimits.put(server, rateLimits);
            }
            rateLimits.put(type, System.currentTimeMillis() + retryAfter);
        }
    }

    /**
     * Checks if a {@link RateLimitType} is rate limited.
     *
     * @param type The type of the rate limit.
     * @return Whether the given type is rate limited or not.
     */
    public boolean isRateLimited(RateLimitType type) {
        return isRateLimited(type, null);
    }

    /**
     * Checks if a {@link RateLimitType} is rate limited.
     *
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @return Whether the given type is rate limited or not.
     */
    public boolean isRateLimited(RateLimitType type, Server server) {
        return getRateLimit(type, server) > 0;
    }

    /**
     * Gets the rate limit the given type.
     * @param type The type of the rate limit.
     * @return Gets the rate limit of the given type in milliseconds or <code>-1</code> if not limited.
     */
    public long getRateLimit(RateLimitType type) {
        return getRateLimit(type, null);
    }

    /**
     * Gets the rate limit the given type.
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @return Gets the rate limit of the given type in milliseconds or <code>-1</code> if not limited.
     */
    public long getRateLimit(RateLimitType type, Server server) {
        if (server == null) { // non-server related
            Long retryAt = rateLimits.get(type);
            if (retryAt == null) {
                return -1;
            }
            long retryAfter = retryAt - System.currentTimeMillis();
            return retryAfter <= 0 ? -1 : retryAfter;
        } else { // server related
            HashMap<RateLimitType, Long> rateLimits = serverRateLimits.get(server);
            if (rateLimits == null) {
                return -1;
            }
            Long retryAt = rateLimits.get(type);
            if (retryAt == null) {
                return -1;
            }
            long retryAfter = retryAt - System.currentTimeMillis();
            return retryAfter <= 0 ? -1 : retryAfter;
        }
    }

}
