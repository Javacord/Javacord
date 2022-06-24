package org.javacord.api.entity.message;

import org.javacord.api.entity.Attachment;

/**
 * This class represents a message attachment.
 */
public interface MessageAttachment extends Attachment {

    /**
     * Gets the message of the attachment.
     *
     * @return The message of the attachment.
     */
    Message getMessage();

}
