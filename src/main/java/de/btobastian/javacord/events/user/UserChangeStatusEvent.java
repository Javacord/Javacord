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
import de.btobastian.javacord.entities.UserStatus;

/**
 * A user change status event.
 */
public class UserChangeStatusEvent extends UserEvent {

    /**
     * The old status of the user.
     */
    private final UserStatus oldStatus;

    /**
     * Creates a new user change status event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldStatus The old status of the user.
     */
    public UserChangeStatusEvent(DiscordApi api, User user, UserStatus oldStatus) {
        super(api, user);
        this.oldStatus = oldStatus;
    }

    /**
     * Gets the old status of the user.
     *
     * @return The old status of the user.
     */
    public UserStatus getOldStatus() {
        return oldStatus;
    }

    /**
     * Gets the new status of the user.
     *
     * @return The new status of the user.
     */
    public UserStatus getNewStatus() {
        // TODO return getUser().getStatus();
        return UserStatus.INVISIBLE;
    }

}
