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
import java.util.concurrent.Future;

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
    public Permissions getPermissions();

    /**
     * Gets the overwritten permissions in the given channel.
     *
     * @param channel The channel to check.
     * @return The overwritten permissions.
     */
    public Permissions getOverwrittenPermissions(Channel channel);

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

    /**
     * Updates the permissions of this role.
     *
     * @param permissions The permissions to set. Roles don't have the {@link PermissionState#NONE} so every
     *                    permission with this state will be replaced with {@link PermissionState#DENIED}.
     * @return A future which tells us if the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updatePermissions(Permissions permissions);

    /**
     * Updates the name of this role.
     *
     * @param name The name to set.
     * @return A future which tells us if the update was successful or not.
     *         If the exception is <code>null</code> the update was successful.
     */
    public Future<Exception> updateName(String name);

    /**
     * Deletes the role.
     *
     * @return A future which tells us if the update was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> delete();

    /**
     * Adds the user to the role.
     * If you want to set more than one role use {@link Server#updateRoles(User, Role[])} or
     *
     * @param user The user to add.
     * @return A future which tells us if the update was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> addUser(User user);

    /**
     * Removes the user from the role.
     *
     * @param user The user to remove.
     * @return A future which tells us if the update was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> removeUser(User user);

}
