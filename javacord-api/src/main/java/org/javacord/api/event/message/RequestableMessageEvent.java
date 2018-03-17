package org.javacord.api.event.message;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

import java.util.concurrent.CompletableFuture;

/**
 * A message event where the message is NOT guaranteed to be in the cache, but can be requested from Discord.
 */
public interface RequestableMessageEvent extends OptionalMessageEvent {

    /**
     * Requests a message from Discord, if it's not cached.
     *
     * @return The message either from the cache or directly from Discord.
     * @see TextChannel#getMessageById(long)
     */
    CompletableFuture<Message> requestMessage();

}
