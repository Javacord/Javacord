package org.javacord.api.event.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;

import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * A message event where the message is NOT guaranteed to be in the cache.
 */
public interface OptionalMessageEvent extends MessageEvent {

    /**
     * Gets the message from the cache.
     *
     * @return The message from the cache.
     */
    Optional<Message> getMessage();

    /**
     * Gets the author of the event's message.
     *
     * @return The author of the event's message.
     * @see Message#getAuthor()
     */
    default Optional<MessageAuthor> getMessageAuthor() {
        return getMessage().map(Message::getAuthor);
    }

    /**
     * Gets a list with all attachments of the event's message.
     *
     * @return A list with all attachments of the event's message.
     * @see Message#getAttachments()
     */
    default Optional<List<MessageAttachment>> getMessageAttachments() {
        return getMessage().map(Message::getAttachments);
    }

    /**
     * Gets the content of the event's message.
     *
     * @return The content of the event's message.
     * @see Message#getContent()
     */
    default Optional<String> getMessageContent() {
        return getMessage().map(Message::getContent);
    }

    /**
     * Gets the readable content of the event's message.
     *
     * @return The readable content of the event's message.
     * @see Message#getReadableContent()
     */
    default Optional<String> getReadableMessageContent() {
        return getMessage().map(Message::getReadableContent);
    }

    /**
     * Gets the link of the event's message.
     *
     * @return The link of the event's message.
     * @see Message#getLink()
     */
    default Optional<URL> getMessageLink() {
        return getMessage().map(Message::getLink);
    }
}
