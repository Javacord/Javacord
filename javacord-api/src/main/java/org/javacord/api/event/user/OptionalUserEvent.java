package org.javacord.api.event.user;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.Event;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * An optional user event.
 */
public interface OptionalUserEvent extends Event {

    /**
     * Gets the id of the user involved in the event.
     *
     * @return The id of the user involved in the event.
     */
    long getUserId();

    /**
     * Gets the id of the user involved in the event.
     *
     * @return The id of the user involved in the event.
     * @see #getUserId()
     */
    default String getUserIdAsString() {
        return String.valueOf(getUserId());
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    default Optional<User> getUser() {
        return getApi().getCachedUserById(getUserId());
    }

    /**
     * Requests a user from Discord with the given id.
     *
     * <p>If the user is in the cache, the user is served from the cache.
     *
     * @return The user.
     */
    default CompletableFuture<User> requestUser() {
        return getApi().getUserById(getUserId());
    }

}
