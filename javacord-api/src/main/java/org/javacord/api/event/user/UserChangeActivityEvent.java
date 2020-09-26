package org.javacord.api.event.user;

import org.javacord.api.entity.activity.Activity;

import java.util.Optional;

/**
 * A user change activity event.
 */
public interface UserChangeActivityEvent extends OptionalUserEvent {

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
