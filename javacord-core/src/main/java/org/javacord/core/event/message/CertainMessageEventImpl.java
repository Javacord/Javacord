package org.javacord.core.event.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CertainMessageEvent;

/**
 * The implementation of {@link CertainMessageEvent}.
 */
public abstract class CertainMessageEventImpl extends MessageEventImpl implements CertainMessageEvent {

    /**
     * The message of the event.
     */
    private final Message message;

    /**
     * Creates a new certain message event.
     *
     * @param message The message.
     */
    public CertainMessageEventImpl(Message message) {
        super(message.getApi(), message.getId(), message.getChannel());
        this.message = message;
    }

    @Override
    public Message getMessage() {
        return message;
    }

}
