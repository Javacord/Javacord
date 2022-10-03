package org.javacord.core.event.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageReplyEvent;

/**
 * The implementation of {@link MessageReplyEvent}.
 */
public class MessageReplyEventImpl extends CertainMessageEventImpl implements MessageReplyEvent {

    private final Message referencedMessage;

    /**
     * Creates a new event instance.
     *
     * @param message The created message.
     * @param referencedMessage The message which is being replied to.
     */
    public MessageReplyEventImpl(Message message, Message referencedMessage) {
        super(message);
        this.referencedMessage = referencedMessage;
    }

    @Override
    public Message getReferencedMessage() {
        return referencedMessage;
    }

}
