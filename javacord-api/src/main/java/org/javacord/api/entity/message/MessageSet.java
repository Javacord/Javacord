package org.javacord.api.entity.message;

import org.javacord.api.entity.DiscordEntity;

import java.util.NavigableSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    /**
     * Deletes all messages in this message set at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> deleteAll() {
        return CompletableFuture.allOf(
                stream().collect(Collectors.groupingBy(DiscordEntity::getApi, Collectors.toList()))
                        .entrySet().stream()
                        .map(entry -> Message.delete(entry.getKey(), entry.getValue()))
                        .toArray(CompletableFuture[]::new));
    }

    @Override
    MessageSet subSet(Message fromElement, boolean fromInclusive, Message toElement, boolean toInclusive);

    @Override
    MessageSet subSet(Message fromElement, Message toElement);

    @Override
    MessageSet headSet(Message toElement, boolean inclusive);

    @Override
    MessageSet headSet(Message toElement);

    @Override
    MessageSet tailSet(Message fromElement);

    @Override
    MessageSet tailSet(Message fromElement, boolean inclusive);
}
