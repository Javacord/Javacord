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
package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;

/**
 * A text channel event which effects a user.
 */
public abstract class TextChannelEvent extends UserEvent {

    /**
     * The text channel of the event.
     */
    private final TextChannel channel;

    /**
     * Creates a new text channel event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public TextChannelEvent(DiscordApi api, User user, TextChannel channel) {
        super(api, user);
        this.channel = channel;
    }

    /**
     * Gets the text channel of the event.
     *
     * @return The text channel of the event.
     */
    public TextChannel getChannel() {
        return channel;
    }
}
