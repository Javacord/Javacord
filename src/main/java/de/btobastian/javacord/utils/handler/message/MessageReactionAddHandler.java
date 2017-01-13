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
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.message.ReactionAddListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Handles the message reaction add packet.
 */
public class MessageReactionAddHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageReactionAddHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageReactionAddHandler(ImplDiscordAPI api) {
        super(api, true, "MESSAGE_REACTION_ADD");
    }

    @Override
    public void handle(JSONObject packet) {
        String userId = packet.getString("user_id");
        String messageId = packet.getString("message_id");

        JSONObject emoji = packet.getJSONObject("emoji");
        boolean isCustomEmoji = !emoji.isNull("id");

        Message message = api.getMessageById(messageId);
        if (message == null) {
            return;
        }

        Reaction reaction = null;
        if (isCustomEmoji) {
            String emojiId = emoji.getString("id");
            if (message.isPrivateMessage()) {
                // Private messages with custom emoji? Maybe with Nitro, but there's no documentation so far.
                return;
            }
            CustomEmoji customEmoji = message.getChannelReceiver().getServer().getCustomEmojiById(emojiId);
            if (customEmoji == null) {
                // We don't know this emoji
                return;
            }
            reaction = ((ImplMessage) message).addCustomEmojiReactionToCache(customEmoji, api.getYourself().getId().equals(userId));
        } else {
            reaction = ((ImplMessage) message).addUnicodeReactionToCache(emoji.getString("name"), api.getYourself().getId().equals(userId));
        }

        if (reaction != null) {
            final User user = api.getCachedUserById(userId);
            if (user != null) {
                final Reaction reactionFinal = reaction;
                listenerExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        List<ReactionAddListener> listeners = api.getListeners(ReactionAddListener.class);
                        synchronized (listeners) {
                            for (ReactionAddListener listener : listeners) {
                                try {
                                    listener.onReactionAdd(api, reactionFinal, user);
                                } catch (Throwable t) {
                                    logger.warn("Uncaught exception in ReactionAddListener!", t);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

}
