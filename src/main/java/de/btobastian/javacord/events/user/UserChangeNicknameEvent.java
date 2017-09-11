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
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

import java.util.Optional;

/**
 * A user change nickname event.
 */
public class UserChangeNicknameEvent extends UserEvent {

    /**
     * The old nickname of the user.
     */
    private final String oldNickname;

    /**
     * The server in which the user changed his nickname.
     */
    private final Server server;

    /**
     * Creates a new user change nickname event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldNickname The old nickname of the user.
     * @param server The server in which the user changed his nickname.
     */
    public UserChangeNicknameEvent(DiscordApi api, User user, String oldNickname, Server server) {
        super(api, user);
        this.oldNickname = oldNickname;
        this.server = server;
    }

    /**
     * Gets the old nickname of the user.
     *
     * @return The old nickname of the user.
     */
    public Optional<String> getOldNickname() {
        return Optional.ofNullable(oldNickname);
    }

    /**
     * Gets the new nickname of the user.
     *
     * @return The new nickname of the user.
     */
    public Optional<String> getNewNickname() {
        // TODO return getUser().getNickname(server);
        return Optional.empty();
    }

}
