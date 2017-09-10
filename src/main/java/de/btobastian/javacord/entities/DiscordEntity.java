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

import de.btobastian.javacord.DiscordApi;

import java.time.Instant;

/**
 * This class represents a Discord entity.
 */
public interface DiscordEntity {

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    DiscordApi getApi();

    /**
     * Gets the id of Discord entity.
     *
     * @return The id of Discord entity.
     * @see <a href="https://discordapp.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    long getId();

    /**
     * Gets the creation date of the Discord entity, calculated from the id.
     *
     * @return The creation date of the Discord entity.
     * @see <a href="https://discordapp.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    default Instant getCreationDate() {
        // The first 42 bits (of the total 64) are the timestamp
        // Discord starts its counter at the first second of 2015
        return Instant.ofEpochMilli((getId() >> 22) + 1420070400000L);
    }

}
