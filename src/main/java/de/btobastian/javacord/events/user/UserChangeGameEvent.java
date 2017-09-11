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
package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.User;

import java.util.Optional;

/**
 * A user change game event.
 */
public class UserChangeGameEvent extends UserEvent {

    /**
     * The old game of the user.
     */
    private final Game oldGame;

    /**
     * Creates a new user change game event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldGame The old game of the user.
     */
    public UserChangeGameEvent(DiscordApi api, User user, Game oldGame) {
        super(api, user);
        this.oldGame = oldGame;
    }

    /**
     * Gets the old game of the user.
     *
     * @return The old game of the user.
     */
    public Optional<Game> getOldGame() {
        return Optional.ofNullable(oldGame);
    }

    /**
     * Gets the new game of the user.
     *
     * @return The new game of the user.
     */
    public Optional<Game> getNewGame() {
        // TODO return getUser().getGame();
        return Optional.empty();
    }

}
