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
package de.btobastian.javacord.utils.handler.message;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listener.message.ReactionRemoveAllListener;
import de.btobastian.javacord.listener.message.ReactionRemoveListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

/**
 * Handles the message reaction remove packet.
 */
public class MessageReactionRemoveAllHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageReactionRemoveAllHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageReactionRemoveAllHandler(ImplDiscordAPI api) {
        super(api, true, "MESSAGE_REACTION_REMOVE_ALL");
    }

    @Override
    public void handle(JSONObject packet) {
        // {"message_id":"269166028959776768","channel_id":"81402706320699392"}
        String messageId = packet.getString("message_id");
        final Message message = api.getMessageById(messageId);
        if (message == null) {
            return;
        }

        final List<Reaction> reactions = message.getReactions();
        ((ImplMessage) message).removeAllReactionsFromCache();

        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<ReactionRemoveAllListener> listeners = api.getListeners(ReactionRemoveAllListener.class);
                synchronized (listeners) {
                    for (ReactionRemoveAllListener listener : listeners) {
                        try {
                            listener.onReactionRemoveAll(api, message, reactions);
                        } catch (Throwable t) {
                            logger.warn("Uncaught exception in ReactionRemoveAllListener!", t);
                        }
                    }
                }
            }
        });

    }

}
