package org.javacord.api.event.message;

import org.javacord.api.entity.message.Message;

import java.util.Optional;

/**
 * A message delete event.
 */
public interface MessageEditEvent extends RequestableMessageEvent {

    /**
     * Gets the updated messages.
     *
     * @return The updated message.
     */
    Message getUpdatedMessage();

    /**
     * Gets the old message with its old content. It will only be present, if the message is in the cache.
     *
     * @return The old message.
     */
    Optional<Message> getOldMessage();

    /**
     * Gets whether this is a special case like embedding links.
     *
     * @return Whether this is a special case like embedding links.
     */
    boolean isSpecialCase();

}
