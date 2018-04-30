package org.javacord.api.event.user;

import org.javacord.api.event.server.ServerEvent;

/**
 * A user change muted event.
 */
public interface UserChangeMutedEvent extends UserEvent, ServerEvent {

    /**
     * Gets the new muted state of the user.
     *
     * @return Whether the user is muted now.
     */
    boolean isNewMuted();

    /**
     * Gets the old muted state of the user.
     *
     * @return Whether the user was muted previously.
     */
    boolean isOldMuted();

}
