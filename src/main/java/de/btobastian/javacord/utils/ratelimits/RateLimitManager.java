/*
 * Copyright (C) 2017 Bastian Oppermann
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

import de.btobastian.javacord.entities.Channel;
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
    // all server related limits
    private final HashMap<Server, HashMap<RateLimitType, Long>> serverRateLimits = new HashMap<>();
    // all channel related limits
    private final HashMap<Channel, HashMap<RateLimitType, Long>> channelRateLimits = new HashMap<>();

    /**
     * Adds a rate limit for the given type.
     *
     * @param type The type of the rate limit.
     * @param retryAfter The retryAfter.
     */
    public void addRateLimit(RateLimitType type, long retryAfter) {
        addRateLimit(type, null, null, retryAfter);
    }

    /**
     * Adds a rate limit for the given type.
     *
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @param channel The channel of the rate limit. Can be <code>null</code> for non-channel related limits.
     * @param retryAfter The retryAfter.
     */
    public void addRateLimit(RateLimitType type, Server server, Channel channel, long retryAfter) {
        if (server == null && channel == null) {
            rateLimits.put(type, System.currentTimeMillis() + retryAfter);
        } else if (channel == null) { // server related
            HashMap<RateLimitType, Long> rateLimits = serverRateLimits.get(server);
            if (rateLimits == null) {
                rateLimits = new HashMap<>();
                serverRateLimits.put(server, rateLimits);
            }
            rateLimits.put(type, System.currentTimeMillis() + retryAfter);
        } else { // channel related
            HashMap<RateLimitType, Long> rateLimits = channelRateLimits.get(channel);
            if (rateLimits == null) {
                rateLimits = new HashMap<>();
                channelRateLimits.put(channel, rateLimits);
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
        return getRateLimit(type, null, null) > 0;
    }

    /**
     * Checks if a {@link RateLimitType} is rate limited.
     *
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @return Whether the given type is rate limited or not.
     */
    public boolean isRateLimited(RateLimitType type, Server server) {
        return getRateLimit(type, server, null) > 0;
    }

    /**
     * Checks if a {@link RateLimitType} is rate limited.
     *
     * @param type The type of the rate limit.
     * @param channel The channel of the rate limit. Can be <code>null</code> for non-channel related limits.
     * @return Whether the given type is rate limited or not.
     */
    public boolean isRateLimited(RateLimitType type, Channel channel) {
        return getRateLimit(type, null, channel) > 0;
    }

    /**
     * Checks if a {@link RateLimitType} is rate limited.
     *
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @param channel The channel of the rate limit. Can be <code>null</code> for non-server related limits.
     * @return Whether the given type is rate limited or not.
     */
    public boolean isRateLimited(RateLimitType type, Server server, Channel channel) {
        return getRateLimit(type, server, channel) > 0;
    }


    /**
     * Gets the rate limit the given type.
     * @param type The type of the rate limit.
     * @return Gets the rate limit of the given type in milliseconds or <code>-1</code> if not limited.
     */
    public long getRateLimit(RateLimitType type) {
        return getRateLimit(type, null, null);
    }

    /**
     * Gets the rate limit the given type.
     * @param type The type of the rate limit.
     * @param server The server of the rate limit. Can be <code>null</code> for non-server related limits.
     * @param channel The channel of the rate limit. Can be <code>null</code> for non-channel related limits.
     * @return Gets the rate limit of the given type in milliseconds or <code>-1</code> if not limited.
     */
    public long getRateLimit(RateLimitType type, Server server, Channel channel) {
        if (server == null && channel == null) { // non-server related
            Long retryAt = rateLimits.get(type);
            if (retryAt == null) {
                return -1;
            }
            long retryAfter = retryAt - System.currentTimeMillis();
            return retryAfter <= 0 ? -1 : retryAfter;
        } else if (channel == null ){ // server related
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
        } else { // channel related
            HashMap<RateLimitType, Long> rateLimits = channelRateLimits.get(channel);
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
