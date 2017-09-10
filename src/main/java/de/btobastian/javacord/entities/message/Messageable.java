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
package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which can receive messages.
 */
public interface Messageable {

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @param embed The embed which should be displayed.
     * @param tts Whether the message should be "text to speech" or not.
     * @param nonce The nonce of the message.
     * @return The sent message.
     */
    CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce);

    /**
     * Sends a message.
     *
     * @param content The content of the message.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(String content) {
        return sendMessage(content, null, false, null);
    }

    /**
     * Sends a message which contains an embed.
     *
     * @param embed The embed which should be displayed.
     * @return The sent message.
     */
    default CompletableFuture<Message> sendMessage(EmbedBuilder embed) {
        return sendMessage(null, embed, false, null);
    }

}
