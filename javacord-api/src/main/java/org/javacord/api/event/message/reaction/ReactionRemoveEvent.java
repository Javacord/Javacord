package org.javacord.api.event.message.reaction;

import org.javacord.api.entity.user.User;

import java.util.Optional;

/**
 * A reaction remove event.
 */
public interface ReactionRemoveEvent extends SingleReactionEvent {

    /**
     * Gets the user whose reaction got removed if the user is cached.
     *
     * @return The user.
     */
    Optional<User> getUser();
}
