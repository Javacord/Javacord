package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeActivityEvent;

/**
 * This listener listens to user activity changes.
 */
@FunctionalInterface
public interface UserChangeActivityListener {

    /**
     * This method is called every time a user changed their activity.
     *
     * @param event The event
     */
    void onUserChangeActivity(UserChangeActivityEvent event);
}
