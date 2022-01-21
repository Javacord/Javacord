package org.javacord.api.event.user;

import org.javacord.api.event.server.ServerEvent;
import java.time.Instant;
import java.util.Optional;

/**
 * A user change timeout event.
 */
public interface UserChangeTimeoutEvent extends UserEvent, ServerEvent {

    /**
     * Gets the new timeout of the user.
     *
     * @return The new timeout of the user.
     */
    Optional<Instant> getNewTimeout();

    /**
     * Gets the old timeout of the user.
     *
     * @return The old timeout of the user.
     */
    Optional<Instant> getOldTimeout();

}
