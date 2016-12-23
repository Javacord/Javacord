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
package de.btobastian.javacord.entities.permissions;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

/**
 * This interface represents a ban. Note: Ban-Objects are nor unique.
 * There may exist more than one instance for the same ban!
 */
public interface Ban {

    /**
     * Gets the banned user.
     *
     * @return The banned used.
     */
    public User getUser();

    /**
     * Gets the server of the ban.
     *
     * @return The server of the ban.
     */
    public Server getServer();

    /**
     * Gets the reason for the ban.
     *
     * @return The reason for the ban. May be <code>null</code>!
     */
    public String getReason();

}