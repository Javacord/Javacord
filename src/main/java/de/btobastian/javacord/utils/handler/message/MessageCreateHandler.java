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
package de.btobastian.javacord.utils.handler.message;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.List;

/**
 * Handles the message create packet.
 */
public class MessageCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageCreateHandler(ImplDiscordAPI api) {
        super(api, true, "MESSAGE_CREATE");
    }

    @Override
    public boolean handle(JSONObject packet) {
        String messageId = packet.getString("id");
        Message message = api.getMessageById(messageId);
        if (message == null) {
            message = new ImplMessage(packet, api, null);
        }
        List<Listener> listeners =  api.getListeners(MessageCreateListener.class);
        synchronized (listeners) {
            for (Listener listener : listeners) {
                ((MessageCreateListener) listener).onMessageCreate(api, message);
            }
        }
        return false;
    }

}
