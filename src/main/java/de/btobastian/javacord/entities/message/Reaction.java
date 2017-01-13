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
package de.btobastian.javacord.entities.message;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.entities.User;

import java.util.List;
import java.util.concurrent.Future;

public interface Reaction {

    /**
     * Gets the message this reaction belongs to.
     *
     * @return The message this reaction belongs to.
     */
    public Message getMessage();

    /**
     * Gets the amount of people reacted with this emoji.
     *
     * @return The amount of people reacted with this emoji.
     */
    public int getCount();

    /**
     * Gets whether you used this reaction or not.
     *
     * @return Whether you used this reaction or not.
     */
    public boolean isUsedByYou();

    /**
     * Gets whether the reaction is a custom emoji or an unicode emoji.
     *
     * @return Whether the reaction is a custom emoji or an unicode emoji.
     */
    public boolean isCustomEmoji();

    /**
     * Gets whether the reaction is an unicode reaction or a custom emoji.
     *
     * @return Whether the reaction is a custom emoji or an unicode reaction.
     */
    public boolean isUnicodeEmoji();

    /**
     * Gets the custom emoji of this reaction.
     *
     * @return The custom emoji of this reaction or <code>null</code> if an unicode reaction was used.
     */
    public CustomEmoji getCustomEmoji();

    /**
     * Gets the unicode emoji of this reaction.
     *
     * @return The unicode emoji of this reaction or <code>null</code> of a custom emoji was used.
     */
    public String getUnicodeEmoji();

    /**
     * Gets a list of users who used this reaction.
     *
     * @return A list of users who used this reaction.
     */
    public Future<List<User>> getUsers();

    /**
     * Gets a list of users who used this reaction.
     *
     * @param callback The callback which will be informed when the reactors were fetched or fetching failed.
     * @return A list of users who used this reaction.
     */
    public Future<List<User>> getUsers(FutureCallback<List<User>> callback);

    /**
     * Removes an user of the reactors list.
     *
     * @param user The user to remove.
     * @return A future which tells us if the removal was successful.
     */
    public Future<Void> removeUser(User user);

}
