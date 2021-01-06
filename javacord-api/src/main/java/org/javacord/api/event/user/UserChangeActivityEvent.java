package org.javacord.api.event.user;

import org.javacord.api.entity.activity.Activity;

import java.util.Collection;

/**
 * A user change activity event.
 */
public interface UserChangeActivityEvent extends OptionalUserEvent {

    /**
     * Gets the old activities of the user.
     *
     * @return The old activities of the user.
     */
    Collection<Activity> getOldActivities();

    /**
     * Gets the new activities of the user.
     *
     * @return The new activities of the user.
     */
    Collection<Activity> getNewActivities();

}
