package org.javacord.event.message.impl;

import org.javacord.entity.message.Message;
import org.javacord.event.message.MessageCreateEvent;

/**
 * The implementation of {@link MessageCreateEvent}.
 */
public class ImplMessageCreateEvent extends ImplCertainMessageEvent implements MessageCreateEvent {

    /**
     * Creates a new event instance.
     *
     * @param message The created message.
     */
    public ImplMessageCreateEvent(Message message) {
        super(message);
    }

}
