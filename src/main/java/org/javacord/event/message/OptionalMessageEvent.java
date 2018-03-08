package org.javacord.event.message;

import org.javacord.entity.message.Message;

import java.util.Optional;

/**
 * A message event where the message is NOT guaranteed to be in the cache.
 */
public interface OptionalMessageEvent extends MessageEvent {

    /**
     * Gets the message from the cache.
     *
     * @return The message from the cache.
     */
    Optional<Message> getMessage();

}
