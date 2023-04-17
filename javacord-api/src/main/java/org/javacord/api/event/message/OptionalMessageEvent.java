package org.javacord.api.event.message;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.exception.MissingIntentException;

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
     * Gets whether you can read the content of the message.
     *
     * @return Whether you can read the content of the message.
     * @see Message#canYouReadContent()
     */
    default Optional<Boolean> canYouReadMessageContent() {
        return getMessage().map(Message::canYouReadContent);
    }

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
     * Gets all attachments of the event's message.
     *
     * @return All attachments of the event's message.
     * @see Message#getAttachments()
     * @throws MissingIntentException If the message is present and not a single of the following requirements is met:
     *                                <ul>
     *                                 <li>The bot has been mentioned in the message.</li>
     *                                 <li>Your are the author of the message.</li>
     *                                 <li>The message is a DM.</li>
     *                                 <li>The {@link Intent#MESSAGE_CONTENT} has been enabled in your code and
     *                                 Discord Developer dashboard</li>
     *                                 </ul>
     */
    default Optional<List<MessageAttachment>> getMessageAttachments() {
        return getMessage().map(Message::getAttachments);
    }

    /**
     * Gets the content of the event's message.
     *
     * @return The content of the event's message.
     * @see Message#getContent()
     * @throws MissingIntentException If the message is present and not a single of the following requirements is met:
     *                                <ul>
     *                                 <li>The bot has been mentioned in the message.</li>
     *                                 <li>Your are the author of the message.</li>
     *                                 <li>The message is a DM.</li>
     *                                 <li>The {@link Intent#MESSAGE_CONTENT} has been enabled in your code and
     *                                 Discord Developer dashboard</li>
     *                                 </ul>
     */
    default Optional<String> getMessageContent() {
        return getMessage().map(Message::getContent);
    }

    /**
     * Gets the readable content of the event's message.
     *
     * @return The readable content of the event's message.
     * @see Message#getReadableContent()
     * @throws MissingIntentException If the message is present and not a single of the following requirements is met:
     *                                <ul>
     *                                 <li>The bot has been mentioned in the message.</li>
     *                                 <li>Your are the author of the message.</li>
     *                                 <li>The message is a DM.</li>
     *                                 <li>The {@link Intent#MESSAGE_CONTENT} has been enabled in your code and
     *                                 Discord Developer dashboard</li>
     *                                 </ul>
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
