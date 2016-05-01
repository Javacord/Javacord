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

/**
 * All known types of rate limits.
 */
public enum RateLimitType {

    /**
     * Private messages were rate limited.
     */
    PRIVATE_MESSAGE(),

    /**
     * Server messages were rate limited.
     */
    SERVER_MESSAGE(),

    /**
     * Name changes were rate limited.
     */
    NAME_CHANGE(),

    /**
     * Private message deletions were rate limited.
     */
    PRIVATE_MESSAGE_DELETE(),

    /**
     * Server message deletions were rate limited.
     */
    SERVER_MESSAGE_DELETE(),

    /**
     * An unknown rate limit.
     */
    UNKNOWN();

    private RateLimitType() {

    }

}
