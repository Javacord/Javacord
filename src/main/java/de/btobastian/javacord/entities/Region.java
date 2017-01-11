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
package de.btobastian.javacord.entities;

/**
 * This enum represents a valid region for a server.
 */
public enum Region {

    AMSTERDAM("amsterdam", "Amsterdam"),
    BRAZIL("brazil", "Brazil"),
    EU_CENTRAL("eu-central", "EU Central"),
    EU_WEST("eu-west", "EU West"),
    FRANKFURT("frankfurt", "Frankfurt"),
    LONDON("london", "London"),
    SINGAPORE("singapore", "Singapore"),
    SYDNEY("sydney", "Sydney"),
    US_CENTRAL("us-central", "US Central"),
    US_EAST("us-east", "US East"),
    US_SOUTH("us-south", "US South"),
    US_WEST("us-west", "US West"),
    /**
     * An unknown region, most likely because discord added new regions.
     */
    UNKNOWN("us-west", "Unknown");

    private final String key;
    private final String name;

    /**
     * Class constructor.
     *
     * @param key The key of the region.
     * @param name The name of the region.
     */
    private Region(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Gets the key of the region which is used for REST-request.
     *
     * @return The key of the region.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the name of the region (which looks nicer than the key :-) ).
     *
     * @return The name of the region.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a region by its key.
     *
     * @param key The key of the region.
     * @return The region with the given key. {@link Region#UNKNOWN} if no region for the given key was found.
     */
    public static Region getRegionByKey(String key) {
        for (Region region : values()) {
            if (region.getKey().equalsIgnoreCase(key) && region != UNKNOWN) {
                return region;
            }
        }
        return UNKNOWN;
    }

}
