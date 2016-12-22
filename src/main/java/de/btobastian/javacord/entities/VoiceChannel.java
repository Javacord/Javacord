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
package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.concurrent.Future;

/**
 * This interface represents a voice channel.
 */
public interface VoiceChannel {

    /**
     * Gets the id of the channel.
     *
     * @return The id of the channel.
     */
    public String getId();

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    public String getName();

    /**
     * Gets the position of the channel.
     *
     * @return The position of the channel.
     */
    public int getPosition();

    /**
     * Gets the server of the channel.
     *
     * @return The server of the channel.
     */
    public Server getServer();

    /**
     * Deletes the channel.
     *
     * @return A future which tells us if the deletion was successful or not.
     */
    public Future<Void> delete();

    /**
     * Gets an invite builder.
     * An invite builder is used to easily create invites.
     *
     * @return An invite builder.
     */
    public InviteBuilder getInviteBuilder();

    /**
     * Gets the overwritten permissions of an user in this channel.
     *
     * @param user The user which overwrites the permissions.
     * @return The overwritten permissions of the user.
     */
    public Permissions getOverwrittenPermissions(User user);

    /**
     * Gets the overwritten permissions of a role in this channel.
     *
     * @param role The role which overwrites the permissions.
     * @return The overwritten permissions of the role.
     */
    public Permissions getOverwrittenPermissions(Role role);

    /**
     * Updates the name of the channel.
     *
     * @param newName The new name of the channel.
     * @return A future which tells us whether the update was successful or not.
     */
    public Future<Void> updateName(String newName);

}
