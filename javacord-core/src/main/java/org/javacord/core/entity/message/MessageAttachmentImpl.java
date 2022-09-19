package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.core.entity.AttachmentImpl;

/**
 * The implementation of {@link MessageAttachment}.
 */
public class MessageAttachmentImpl extends AttachmentImpl implements MessageAttachment {

    /**
     * The message of the attachment.
     */
    private final Message message;

    /**
     * Creates a new message attachment.
     *
     * @param message The message of the attachment.
     * @param data The data of the attachment.
     */
    public MessageAttachmentImpl(final Message message, final JsonNode data) {
        super(message.getApi(), data);
        this.message = message;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
