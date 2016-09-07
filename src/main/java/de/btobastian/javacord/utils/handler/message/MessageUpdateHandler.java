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
import de.btobastian.javacord.listener.message.MessageEditListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

/**
 * Handles the message update packet.
 */
public class MessageUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "MESSAGE_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        String messageId = packet.getString("id");
        final Message message = api.getMessageById(messageId);
        if (message == null) {
            return;
        }
        final String oldContent = message.getContent();
        if (!packet.has("content")) {
            return;
        }
        ((ImplMessage) message).setContent(packet.getString("content"));
        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<MessageEditListener> listeners = api.getListeners(MessageEditListener.class);
                synchronized (listeners) {
                    for (MessageEditListener listener : listeners) {
                        try {
                            listener.onMessageEdit(api, message, oldContent);
                        } catch (Throwable t) {
                            logger.warn("Uncaught exception in MessageEditListener!", t);
                        }
                    }
                }
            }
        });
    }

}
