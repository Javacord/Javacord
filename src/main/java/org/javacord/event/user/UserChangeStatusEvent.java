package org.javacord.event.user;

import org.javacord.entity.user.UserStatus;

/**
 * A user change status event.
 */
public interface UserChangeStatusEvent extends UserEvent {

    /**
     * Gets the old status of the user.
     *
     * @return The old status of the user.
     */
    UserStatus getOldStatus();

    /**
     * Gets the new status of the user.
     *
     * @return The new status of the user.
     */
    UserStatus getNewStatus();

}
