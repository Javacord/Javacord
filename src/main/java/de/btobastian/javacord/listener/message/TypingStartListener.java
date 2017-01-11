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
package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listener.Listener;

/**
 * This listener listens to the typing of user.
 */
public interface TypingStartListener extends Listener {

    /**
     * This method is called every time a user starts typing.
     * Typing stops after 5 seconds or if the user sends a message.
     *
     * @param api The api.
     * @param user The user who starts typing.
     * @param channel The channel where the users is typing. <code>Null</code> if the user is typing in private a chat.
     */
    public void onTypingStart(DiscordAPI api, User user, Channel channel);

}
