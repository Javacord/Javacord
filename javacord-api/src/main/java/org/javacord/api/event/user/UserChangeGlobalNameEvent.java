package org.javacord.api.event.user;

import java.util.Optional;

/**
 * A user change global name event.
 */
public interface UserChangeGlobalNameEvent extends UserEvent {

    /**
     * Gets the old global name of the user.
     *
     * @return The old global name of the user.
     */
    Optional<String> getOldGlobalName();

    /**
     * Gets the new global name of the user.
     *
     * @return The new global name of the user.
     */
    Optional<String> getNewGlobalName();
}
