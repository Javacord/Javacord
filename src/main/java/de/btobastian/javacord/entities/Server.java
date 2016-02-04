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

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * This interface represents a discord server (also known as guild).
 */
public interface Server {

    /**
     * Gets the unique id of the server.
     *
     * @return The unique id of the server.
     */
    public String getId();

    /**
     * Gets the name of the server.
     *
     * @return The name of the server.
     */
    public String getName();

    /**
     * Deletes or leaves the server.
     *
     * @return A future which tells us if the deletion was successful or not.
     *         If the exception is <code>null</code> the deletion was successful.
     */
    public Future<Exception> deleteOrLeave();

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     *         <code>Null</code> if the server has no channel with the given id.
     */
    public Channel getChannelById(String id);

    /**
     * Gets a collection with all channels of the server.
     *
     * @return A collection with all channels of the server.
     */
    public Collection<Channel> getChannels();

    /**
     * Gets a User by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     *         <code>Null</code> if the user is no member of this server.
     */
    public User getMemberById(String id);

    /**
     * Gets a collection with all members on this server.
     *
     * @return A collection with all members on this server.
     */
    public Collection<User> getMembers();

    /**
     * Checks if an user is a member of this server.
     *
     * @param user The user to check.
     * @return Whether the user is a member or not.
     */
    public boolean isMember(User user);

    /**
     * Checks if an user is a member of this server.
     *
     * @param userId The id of the user to check.
     * @return Whether the user is a member or not.
     */
    public boolean isMember(String userId);

}
