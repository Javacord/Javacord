package org.javacord.core.event.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

/**
 * The implementation of {@link MessageCreateEvent}.
 */
public class MessageCreateEventImpl extends CertainMessageEventImpl implements MessageCreateEvent {

    /**
     * Creates a new event instance.
     *
     * @param message The created message.
     */
    public MessageCreateEventImpl(Message message) {
        super(message);
    }

}
