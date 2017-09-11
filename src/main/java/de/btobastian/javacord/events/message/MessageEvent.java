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
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.events.Event;

/**
 * A message event.
 */
public abstract class MessageEvent extends Event {

    /**
     * The id of the message.
     */
    private final long messageId;

    /**
     * The text channel in which the message was sent.
     */
    private final TextChannel channel;

    /**
     * Creates a new message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api);
        this.messageId = messageId;
        this.channel = channel;
    }

    /**
     * Gets the id of the message.
     *
     * @return The id of the message.
     */
    public long getMessageId() {
        return messageId;
    }

    /**
     * Gets the channel in which the message was sent.
     *
     * @return The channel in which the message was sent.
     */
    public TextChannel getChannel() {
        return channel;
    }

}
