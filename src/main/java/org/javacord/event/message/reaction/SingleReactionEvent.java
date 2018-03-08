package org.javacord.event.message.reaction;

import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.message.Reaction;
import org.javacord.entity.user.User;
import org.javacord.event.message.RequestableMessageEvent;
import org.javacord.event.user.UserEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A single reaction event.
 */
public interface SingleReactionEvent extends ReactionEvent, UserEvent {

    /**
     * Gets the emoji of the event.
     *
     * @return The emoji.
     */
    Emoji getEmoji();

    /**
     * Gets the reaction if the message is cached and the reaction exists.
     *
     * @return The reaction.
     */
    Optional<Reaction> getReaction();

    /**
     * Gets the reaction if it exists.
     * If the message is not cached, it will be requested from Discord first.
     *
     * @return The reaction.
     * @see RequestableMessageEvent#requestMessage()
     */
    CompletableFuture<Optional<Reaction>> requestReaction();

    /**
     * Gets the amount of users who used the reaction if the message is cached.
     *
     * @return The amount of users who used the reaction.
     */
    Optional<Integer> getCount();

    /**
     * Gets the amount of users who used the reaction.
     * If the message is not cached, it will be requested from Discord first.
     *
     * @return The amount of users who used the reaction.
     * @see RequestableMessageEvent#requestMessage()
     */
    CompletableFuture<Integer> requestCount();

    /**
     * Gets a list with all users who used the reaction.
     *
     * @return A list with all users who used the reaction.
     */
    CompletableFuture<List<User>> getUsers();

}
