package org.javacord.event.user.impl;

import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;
import org.javacord.event.user.UserChangeStatusEvent;

/**
 * The implementation of {@link UserChangeStatusEvent}.
 */
public class ImplUserChangeStatusEvent extends ImplUserEvent implements UserChangeStatusEvent {

    /**
     * The new status of the user.
     */
    private final UserStatus newStatus;

    /**
     * The old status of the user.
     */
    private final UserStatus oldStatus;

    /**
     * Creates a new user change status event.
     *
     * @param user The user of the event.
     * @param newStatus The new status of the user.
     * @param oldStatus The old status of the user.
     */
    public ImplUserChangeStatusEvent(User user, UserStatus newStatus, UserStatus oldStatus) {
        super(user);
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }

    @Override
    public UserStatus getOldStatus() {
        return oldStatus;
    }

    @Override
    public UserStatus getNewStatus() {
        return newStatus;
    }

}
