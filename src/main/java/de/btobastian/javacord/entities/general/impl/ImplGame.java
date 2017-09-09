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
package de.btobastian.javacord.entities.general.impl;

import de.btobastian.javacord.entities.general.Game;
import de.btobastian.javacord.entities.general.GameType;

import java.util.Optional;

/**
 * The implementation of {@link de.btobastian.javacord.entities.general.Game}.
 */
public class ImplGame implements Game {

    private final GameType type;
    private final String name;
    private final String streamingUrl;

    /**
     * Creates a new game object.
     *
     * @param type The type of the game.
     * @param name The name of the game.
     * @param streamingUrl The streaming url of the game. May be <code>null</code>.
     */
    public ImplGame(GameType type, String name, String streamingUrl) {
        this.type = type;
        this.name = name;
        this.streamingUrl = streamingUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getStreamingUrl() {
        return Optional.ofNullable(streamingUrl);
    }

    @Override
    public GameType getType() {
        return type;
    }

}
