package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeGameEvent;

/**
 * This listener listens to user name changes.
 */
@FunctionalInterface
public interface UserChangeNameListener {

    /**
     * This method is called every time a user changed it's name.
     *
     * @param event The event.
     */
    void onUserChangeName(UserChangeGameEvent event);

}