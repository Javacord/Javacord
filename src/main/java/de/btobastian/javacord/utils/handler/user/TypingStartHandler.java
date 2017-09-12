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
package de.btobastian.javacord.utils.handler.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.events.user.UserStartTypingEvent;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the typing start packet.
 */
public class TypingStartHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public TypingStartHandler(DiscordApi api) {
        super(api, true, "TYPING_START");
    }

    @Override
    public void handle(JSONObject packet) {
        long userId = Long.parseLong(packet.getString("user_id"));
        long channelId = Long.parseLong(packet.getString("channel_id"));
        api.getTextChannelById(channelId).ifPresent(channel -> api.getUserById(userId).ifPresent(user -> {
            UserStartTypingEvent event = new UserStartTypingEvent(api, user, channel);
            listenerExecutorService.submit(() -> {
                List<UserStartTypingListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getUserStartTypingListeners());
                if (channel instanceof ServerTextChannel) {
                    listeners.addAll(((ServerTextChannel) channel).getServer().getUserStartTypingListeners());
                }
                listeners.addAll(user.getUserStartTypingListeners());
                listeners.addAll(api.getUserStartTypingListeners());
                listeners.forEach(listener -> listener.onUserStartTyping(event));
            });
        }));
    }

}