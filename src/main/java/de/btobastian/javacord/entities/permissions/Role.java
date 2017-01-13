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
package de.btobastian.javacord.entities.permissions;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VoiceChannel;

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
     * Gets the overwritten permissions in the given voice channel.
     *
     * @param channel The voice channel to check.
     * @return The overwritten permissions.
     */
    public Permissions getOverwrittenPermissions(VoiceChannel channel);

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
     * Gets whether the role is mentionable or not.
     *
     * @return Whether the role is mentionable or not.
     */
    public boolean isMentionable();

    /**
     * Gets whether this role is managed by an integration or not.
     *
     * @return Whether this role is managed by an integration or not.
     */
    public boolean isManaged();

    /**
     * Gets the tag used to mention a role.
     *
     * @return The tag used to mention a role.
     */
    public String getMentionTag();

    /**
     * Updates the permissions of this role.
     * If you want to update other things like name, color, etc. use
     * {@link #update(String, Color, boolean, Permissions)} or your previous updates may be overwritten.
     *
     * @param permissions The permissions to set. Roles don't have the {@link PermissionState#NONE} so every
     *                    permission with this state will be replaced with {@link PermissionState#DENIED}.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> updatePermissions(Permissions permissions);

    /**
     * Updates the name of this role.
     * If you want to update other things like permissions, color, etc. use
     * {@link #update(String, Color, boolean, Permissions)} or your previous updates may be overwritten.
     *
     * @param name The name to set.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> updateName(String name);

    /**
     * Updates the color of this role.
     * If you want to update other things like permissions, name, etc. use
     * {@link #update(String, Color, boolean, Permissions)} or your previous updates may be overwritten.
     *
     * @param color The color to set.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> updateColor(Color color);

    /**
     * Updates the hoist of this role.
     * If you want to update other things like permissions, color, etc. use
     * {@link #update(String, Color, boolean, Permissions)} or your previous updates may be overwritten.
     *
     * @param hoist The hoist to set.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> updateHoist(boolean hoist);

    /**
     * Updates the role.
     *
     * @param name The new name of the role.
     * @param color The new color of the role.
     * @param hoist The new hoist of the role.
     * @param permissions The new permissions of the role.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> update(String name, Color color, boolean hoist, Permissions permissions);

    /**
     * Deletes the role.
     *
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> delete();

    /**
     * Adds the user to the role.
     * If you want to set more than one role use {@link Server#updateRoles(User, Role[])} or
     *
     * @param user The user to add.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> addUser(User user);

    /**
     * Removes the user from the role.
     *
     * @param user The user to remove.
     * @return A future which tells us if the update was successful or not.
     */
    public Future<Void> removeUser(User user);

}
