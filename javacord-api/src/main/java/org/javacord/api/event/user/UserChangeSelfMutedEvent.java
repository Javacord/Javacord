package org.javacord.api.event.user;

import org.javacord.api.event.server.ServerEvent;

/**
 * A user change self-muted event.
 */
public interface UserChangeSelfMutedEvent extends UserEvent, ServerEvent {

    /**
     * Gets the new self-muted state of the user.
     *
     * @return Whether the user is self-muted now.
     */
    boolean isNewSelfMuted();

    /**
     * Gets the old self-muted state of the user.
     *
     * @return Whether the user was self-muted previously.
     */
    boolean isOldSelfMuted();

}
