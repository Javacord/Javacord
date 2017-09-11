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

/**
 * A message event where the message is guaranteed to be in the cache.
 */
public abstract class CertainMessageEvent extends MessageEvent {

    /**
     * The message of the event.
     */
    private final Message message;

    /**
     * Creates a new certain message event.
     *
     * @param api The discord api instance.
     * @param message The message.
     */
    public CertainMessageEvent(DiscordApi api, Message message) {
        super(api, message.getId(), message.getChannel());
        this.message = message;
    }

    /**
     * Gets the message of the event.
     *
     * @return The message of the event.
     */
    public Message getMessage() {
        return message;
    }

}
