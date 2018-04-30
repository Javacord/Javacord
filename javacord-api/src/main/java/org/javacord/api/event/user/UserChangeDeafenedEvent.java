package org.javacord.api.event.user;

import org.javacord.api.event.server.ServerEvent;

/**
 * A user change deafened event.
 */
public interface UserChangeDeafenedEvent extends UserEvent, ServerEvent {

    /**
     * Gets the new deafened state of the user.
     *
     * @return Whether the user is deafened now.
     */
    boolean isNewDeafened();

    /**
     * Gets the old deafened state of the user.
     *
     * @return Whether the user was deafened previously.
     */
    boolean isOldDeafened();

}
