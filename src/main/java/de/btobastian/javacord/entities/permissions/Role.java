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

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

import java.awt.*;
import java.util.List;

/**
 * This class represents a discord role, e.g. "moderator".
 */
public interface Role {

    /**
     * Gets the id of the role.
     *
     * @return The id of the role.
     */
    public String getId();

    /**
     * Gets the name of the role.
     *
     * @return The name of the role.
     */
    public String getName();

    /**
     * Gets the server of this role.
     *
     * @return The server of the role.
     */
    public Server getServer();

    /**
     * Gets the permissions of this role.
     *
     * @return The permissions of this role.
     */
    public Permissions getPermission();

    /**
     * Gets the overridden permissions in the given channel.
     *
     * @param channel The channel to check.
     * @return The overridden permissions.
     */
    public Permissions getOverriddenPermissions(Channel channel);

    /**
     * Gets all users with this role.
     *
     * @return All users with this role.
     */
    public List<User> getUsers();

    /**
     * Gets the position of the role.
     *
     * @return The position of the role.
     */
    public int getPosition();

    /**
     * Gets if the role's users should be displayed separately.
     *
     * @return Whether to display the role's users separately or not.
     */
    public boolean getHoist();

    /**
     * Gets the color of the role.
     *
     * @return The color of the role.
     */
    public Color getColor();

}
