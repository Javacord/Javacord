package org.javacord.api.event.message;

import org.javacord.api.entity.message.Message;

/**
 * A message reply event.
 */
public interface MessageReplyEvent extends CertainMessageEvent {

    /**
     * Gets the message referenced by the message of this event.
     *
     * @return The message which was referenced.
     */
    Message getReferencedMessage();

}
