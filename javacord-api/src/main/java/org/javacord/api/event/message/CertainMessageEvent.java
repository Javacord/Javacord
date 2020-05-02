package org.javacord.api.event.message;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;

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
     * Checks if the event's message was sent in a {@link ChannelType#PRIVATE_CHANNEL private channel}.
     *
     * @return Whether or not the event's message was sent in a private channel.
     * @see Message#isPrivateMessage()
     */
    default boolean isPrivateMessage() {
        return getMessage().isPrivateMessage();
    }

    /**
     * Checks if the event's message was sent in a {@link ChannelType#SERVER_TEXT_CHANNEL server channel}.
     *
     * @return Whether or not the event's message was sent in a server channel.
     * @see Message#isServerMessage()
     */
    default boolean isServerMessage() {
        return getMessage().isServerMessage();
    }

    /**
     * Checks if the event's message was sent in a {@link ChannelType#GROUP_CHANNEL group channel}.
     *
     * @return Whether or not the event's message was sent in a group channel.
     * @see Message#isPrivateMessage()
     */
    default boolean isGroupMessage() {
        return getMessage().isGroupMessage();
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
     * Gets a list with all attachments of the event's message.
     *
     * @return A list with all attachments of the event's message.
     * @see Message#getAttachments()
     */
    default List<MessageAttachment> getMessageAttachments() {
        return getMessage().getAttachments();
    }

    /**
     * Gets the content of the event's message.
     *
     * @return The content of the event's message.
     * @see Message#getContent()
     */
    default String getMessageContent() {
        return getMessage().getContent();
    }

    /**
     * Gets the readable content of the event's message.
     *
     * @return The readable content of the event's message.
     * @see Message#getReadableContent()
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
