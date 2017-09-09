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
package de.btobastian.javacord.entities.general;

import java.util.Optional;

/**
 * This class represents a game as it is displayed in Discord.
 */
public interface Game {

    /**
     * Gets the type of the game.
     *
     * @return The type of the game.
     */
    GameType getType();

    /**
     * Gets the name of the game.
     *
     * @return The name of the game.
     */
    String getName();

    /**
     * Gets the streaming url of the game.
     *
     * @return The streaming url of the game.
     */
    Optional<String> getStreamingUrl();

}
