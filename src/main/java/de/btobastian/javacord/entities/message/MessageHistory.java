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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This interface represents a message history.
 */
public interface MessageHistory {

    /**
     * Gets the message with the given id.
     *
     * @param id The id of the message.
     * @return The message with the given id. <code>Null</code> if no message with the given id exists.
     */
    public Message getMessageById(String id);

    /**
     * Gets an iterator for all messages in the history.
     *
     * @return The iterator for the messages.
     */
    public Iterator<Message> iterator();

    /**
     * Gets a collection with all fetched messages.
     *
     * @return A collection with all fetched messages.
     */
    public Collection<Message> getMessages();

    /**
     * Gets the newest message in the history.
     *
     * @return The newest message in the history.
     */
    public Message getNewestMessage();

    /**
     * Gets the oldest message in the history.
     *
     * @return The oldest message in the history.
     */
    public Message getOldestMessage();

    /**
     * Gets a sorted list with all fetched messages.
     *
     * @return A sorted list with all fetched messages.
     */
    public List<Message> getMessagesSorted();

}
