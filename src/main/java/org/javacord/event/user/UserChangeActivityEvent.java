package org.javacord.event.user;

import org.javacord.entity.activity.Activity;

import java.util.Optional;

/**
 * A user change activity event.
 */
public interface UserChangeActivityEvent extends UserEvent {

    /**
     * Gets the old activity of the user.
     *
     * @return The old activity of the user.
     */
    Optional<Activity> getOldActivity();

    /**
     * Gets the new activity of the user.
     *
     * @return The new activity of the user.
     */
    Optional<Activity> getNewActivity();

}
