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
package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.events.Event;

/**
 * A message create event.
 */
public class MessageCreateEvent extends Event {

    /**
     * The created message.
     */
    private final Message message;

    /**
     * Creates a new event instance.
     *
     * @param api The api instance of the event.
     * @param message The created message.
     */
    public MessageCreateEvent(DiscordApi api, Message message) {
        super(api);
        this.message = message;
    }

    /**
     * Gets the created message.
     *
     * @return The created message.
     */
    public Message getMessage() {
        return message;
    }

}
