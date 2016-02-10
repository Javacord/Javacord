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

import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.permissions.Permissions;

import java.util.concurrent.Future;

/**
 * This interface represents a channel.
 */
public interface Channel extends MessageReceiver {

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
     * Gets the topic of the channel.
     *
     * @return The topic of the channel.
     */
    public String getTopic();

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
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> delete();

    /**
     * Shows the "is typing.." status for 5 seconds.
     */
    public void type();

    /**
     * Gets an invite builder.
     * An invite builder is used to easily create invites.
     *
     * @return An invite builder.
     */
    public InviteBuilder getInviteBuilder();

    /**
     * Gets the overridden permissions of an user in this channel.
     *
     * @param user The user which overrides the permissions.
     * @return The overridden permissions of the user.
     */
    public Permissions getOverriddenPermissions(User user);

}
