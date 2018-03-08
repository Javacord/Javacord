package org.javacord.event.message.impl;

import org.javacord.entity.message.Message;
import org.javacord.event.message.CertainMessageEvent;

/**
 * The implementation of {@link CertainMessageEvent}.
 */
public abstract class ImplCertainMessageEvent extends ImplMessageEvent implements CertainMessageEvent {

    /**
     * The message of the event.
     */
    private final Message message;

    /**
     * Creates a new certain message event.
     *
     * @param message The message.
     */
    public ImplCertainMessageEvent(Message message) {
        super(message.getApi(), message.getId(), message.getChannel());
        this.message = message;
    }

    @Override
    public Message getMessage() {
        return message;
    }

}
