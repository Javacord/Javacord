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
import de.btobastian.javacord.entities.User;

/**
 * A user change name event.
 */
public class UserChangeNameEvent extends UserEvent {

    /**
     * The old name of the user.
     */
    private final String oldName;

    /**
     * Creates a new user change name event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldName The old name of the user.
     */
    public UserChangeNameEvent(DiscordApi api, User user, String oldName) {
        super(api, user);
        this.oldName = oldName;
    }

    /**
     * Gets the old name of the user.
     *
     * @return The old name of the user.
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Gets the new name of the user.
     *
     * @return The new name of the user.
     */
    public String getNewName() {
        // TODO return getUser().getName();
        return "";
    }

}
