package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeNameEvent;

/**
 * This listener listens to user name changes.
 */
@FunctionalInterface
public interface UserChangeNameListener {

    /**
     * This method is called every time a user changes their name.
     *
     * @param event The event.
     */
    void onUserChangeName(UserChangeNameEvent event);

}