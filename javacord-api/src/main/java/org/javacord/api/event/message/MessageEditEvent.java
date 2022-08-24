package org.javacord.api.event.message;

import org.javacord.api.entity.message.Message;
import java.util.Optional;

/**
 * A message delete event.
 */
public interface MessageEditEvent extends CertainMessageEvent {

    /**
     * Gets the updated message.
     *
     * @return The updated message.
     */
    @Override
    Message getMessage();

    /**
     * Gets the old message with its old content. It will only be present if the message
     * was in the cache before this event.
     *
     * @return The old message.
     */
    Optional<Message> getOldMessage();

    /**
     * Whether this event represents a real change of the contents of this message.
     *
     * @return true if the original author updated the contents of this message; false otherwise
     */
    boolean isActualEdit();

}
