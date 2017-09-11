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
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.Embed;

import java.util.Optional;

/**
 * A message delete event.
 */
public class MessageEditEvent extends OptionalMessageEvent {

    /**
     * The new content of the message.
     */
    private final String newContent;

    /**
     * The new embed of the message. May be <code>null</code>.
     */
    private final Embed newEmbed;

    /**
     * Creates a new message edit event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageEditEvent(DiscordApi api, long messageId, TextChannel channel, String newContent, Embed newEmbed) {
        super(api, messageId, channel);
        this.newContent = newContent;
        this.newEmbed = newEmbed;
    }

    /**
     * Gets the new content of the message.
     *
     * @return The new content of the message.
     */
    public String getNewContent() {
        return newContent;
    }

    /**
     * Gets the old content of the message. It will only be present, if the message is in the cache.
     *
     * @return The old content of the message.
     */
    public Optional<String> getOldContent() {
        return getMessage().map(Message::getContent);
    }

    /**
     * Gets the new embed of the message. May not be present, if the new message does not have an embed.
     *
     * @return The new embed of the message.
     */
    public Optional<Embed> getNewEmbed() {
        return Optional.ofNullable(newEmbed);
    }

    /**
     * Gets the old embed of the message.
     * The outer optional is only present, if the old message is in the cache.
     * The inner optional is only present, if the old message had an embed.
     *
     * @return The old embed of the message.
     */
    public Optional<Optional<Embed>> getOldEmbed() {
        return getMessage().map(Message::getEmbed);
    }

}
