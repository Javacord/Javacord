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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A message event where the message is NOT guaranteed to be in the cache.
 */
public abstract class OptionalMessageEvent extends MessageEvent {

    /**
     * Creates a new optional message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public OptionalMessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

    /**
     * Gets the message from the cache.
     *
     * @return The message from the cache.
     */
    public Optional<Message> getMessage() {
        // TODO
        return Optional.empty();
    }

    /**
     * Requests a message from Discord, if it's not cached.
     *
     * @return The message either from the cache or directly from Discord.
     */
    public CompletableFuture<Message> requestMessage() {
        CompletableFuture<Message> future = new CompletableFuture<>();
        Optional<Message> message = getMessage();
        message.ifPresent(future::complete);
        if (!message.isPresent()) {
            // TODO request message
            future.completeExceptionally(new IllegalStateException("Not implemented atm!"));
        }
        return future;
    }

}
