package de.btobastian.javacord.entities.message;

import java.util.NavigableSet;
import java.util.Optional;

/**
 * This class represents an unmodifiable set of messages that is always sorted from oldest
 * to newest according to the natural ordering of {@link Message}s.
 */
public interface MessageSet extends NavigableSet<Message> {

    /**
     * Gets the oldest message in the history.
     *
     * @return The oldest message in the history.
     */
    default Optional<Message> getOldestMessage() {
        return isEmpty() ? Optional.empty() : Optional.of(first());
    }

    /**
     * Gets the newest message in the history.
     *
     * @return The newest message in the history.
     */
    default Optional<Message> getNewestMessage() {
        return isEmpty() ? Optional.empty() : Optional.of(last());
    }

    @Override
    MessageSet subSet(Message fromElement, boolean fromInclusive, Message toElement, boolean toInclusive);

    @Override
    MessageSet headSet(Message toElement, boolean inclusive);

    @Override
    MessageSet tailSet(Message fromElement, boolean inclusive);

    @Override
    MessageSet subSet(Message fromElement, Message toElement);

    @Override
    MessageSet headSet(Message toElement);

    @Override
    MessageSet tailSet(Message fromElement);

}
