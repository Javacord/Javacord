package org.javacord.api.event.message;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.exception.MissingIntentException;

import java.net.URL;
import java.util.List;

/**
 * A message event where the message is guaranteed to be in the cache.
 */
public interface CertainMessageEvent extends MessageEvent {

    /**
     * Gets the message of the event.
     *
     * @return The message of the event.
     */
    Message getMessage();

    /**
     * Checks if the bot can read the content of the message.
     *
     * @return Whether the bot can read the content of the message.
     */
    default boolean canYouReadContent() {
        return getMessage().canYouReadContent();
    }

    /**
     * Checks if the event's message was sent in a {@link ChannelType#PRIVATE_CHANNEL private channel}.
     *
     * @return Whether the event's message was sent in a private channel.
     * @see Message#isPrivateMessage()
     */
    default boolean isPrivateMessage() {
        return getMessage().isPrivateMessage();
    }

    /**
     * Checks if the event's message was sent in a {@link ChannelType#SERVER_TEXT_CHANNEL server channel}.
     *
     * @return Whether the event's message was sent in a server channel.
     * @see Message#isServerMessage()
     */
    default boolean isServerMessage() {
        return getMessage().isServerMessage();
    }

    /**
     * Gets the author of the event's message.
     *
     * @return The author of the event's message.
     * @see Message#getAuthor()
     */
    default MessageAuthor getMessageAuthor() {
        return getMessage().getAuthor();
    }

    /**
     * Gets all attachments of the event's message.
     *
     * @return All attachments of the event's message.
     * @see Message#getAttachments()
     * @throws MissingIntentException If not a single of the following requirements is met:
     *                                <ul>
     *                                 <li>The bot has been mentioned in the message.</li>
     *                                 <li>Your are the author of the message.</li>
     *                                 <li>The message is a DM.</li>
     *                                 <li>The {@link Intent#MESSAGE_CONTENT} has been enabled in your code and
     *                                 Discord Developer dashboard</li>
     *                                 </ul>
     */
    default List<MessageAttachment> getMessageAttachments() {
        return getMessage().getAttachments();
    }

    /**
     * Gets the content of the event's message.
     *
     * @return The content of the event's message.
     * @see Message#getContent()
     * @throws MissingIntentException If not a single of the following requirements is met:
     *                                <ul>
     *                                 <li>The bot has been mentioned in the message.</li>
     *                                 <li>Your are the author of the message.</li>
     *                                 <li>The message is a DM.</li>
     *                                 <li>The {@link Intent#MESSAGE_CONTENT} has been enabled in your code and
     *                                 Discord Developer dashboard</li>
     *                                 </ul>
     */
    default String getMessageContent() {
        return getMessage().getContent();
    }

    /**
     * Gets the readable content of the event's message.
     *
     * @return The readable content of the event's message.
     * @see Message#getReadableContent()
     * @throws MissingIntentException If not a single of the following requirements is met:
     *                                <ul>
     *                                 <li>The bot has been mentioned in the message.</li>
     *                                 <li>Your are the author of the message.</li>
     *                                 <li>The message is a DM.</li>
     *                                 <li>The {@link Intent#MESSAGE_CONTENT} has been enabled in your code and
     *                                 Discord Developer dashboard</li>
     *                                 </ul>
     */
    default String getReadableMessageContent() {
        return getMessage().getReadableContent();
    }

    /**
     * Gets the link of the event's message.
     *
     * @return The link of the event's message.
     * @see Message#getLink()
     */
    default URL getMessageLink() {
        return getMessage().getLink();
    }
}
