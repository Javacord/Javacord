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

/**
 * Represents a game type.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#game-object-game-types">Discord docs</a>
 */
public enum GameType {

    /**
     * Represents a normal game, represented as "Playing Half-Life 3" for example.
     */
    GAME(0),

    /**
     * Represents streaming a game, represented as "Streaming Half-Life 3" for example.
     */
    STREAMING(1);

    private final int id;

    /**
     * Class constructor.
     *
     * @param id The id of the game type
     */
    GameType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the game type.
     *
     * @return The id of the game type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the game type by it's id.
     *
     * @param id The id of the game type
     * @return The game type with the given id or {@link GameType#GAME} if unknown id.
     */
    public static GameType getGameTypeById(int id) {
        switch (id) {
            case 0:
                return GAME;
            case 1:
                return STREAMING;
            default:
                return GAME;
        }
    }

}
