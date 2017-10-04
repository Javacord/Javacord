package de.btobastian.javacord.entities.message;

import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents a history of messages in a specific channel.
 */
public interface MessageHistory {

    /**
     * Gets an ordered list with all messages.
     * The message with the lowest index is the newest message.
     * The message with the largest index is the oldest message.
     *
     * @return An ordered list with all messages.
     */
    List<Message> getMessages();

    /**
     * Gets a stream with all messages in the history.
     *
     * @return A stream with all messages in the history.
     */
    default Stream<Message> stream() {
        return getMessages().stream();
    }

    /**
     * Gets the oldest message in the history.
     *
     * @return The oldest message in the history.
     * @throws IllegalStateException If the messages list is empty.
     */
    default Message getOldestMessage() throws IllegalStateException {
        if (getMessages().isEmpty()) {
            throw new IllegalStateException("Cannot get oldest message because the history does not contain messages!");
        }
        return getMessages().get(getMessages().size() - 1);
    }

    /**
     * Gets the newest message in the history.
     *
     * @return The newest message in the history.
     * @throws IllegalStateException If the messages list is empty.
     */
    default Message getNewestMessage() throws IllegalStateException {
        if (getMessages().isEmpty()) {
            throw new IllegalStateException("Cannot get newest message because the history does not contain messages!");
        }
        return getMessages().get(0);
    }

}
