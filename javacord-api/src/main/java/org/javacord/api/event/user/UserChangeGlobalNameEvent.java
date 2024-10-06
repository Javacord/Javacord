package org.javacord.api.event.user;

import java.util.Optional;

/**
 * A user global name change event.
 */
public interface UserChangeGlobalNameEvent extends UserEvent {

    /**
     * Gets the new global name of the user.
     *
     * @return The new global name of the user.
     */
    Optional<String> getNewGlobalName();

    /**
     * Gets the old global name of the user.
     *
     * @return The new global name of the user.
     */
    Optional<String> getOldGlobalName();

}
