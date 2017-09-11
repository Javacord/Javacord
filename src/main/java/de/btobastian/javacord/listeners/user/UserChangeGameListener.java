package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeGameEvent;

/**
 * This listener listens to user game changes.
 */
@FunctionalInterface
public interface UserChangeGameListener {

    /**
     * This method is called every time a user changes their game.
     *
     * @param event The event
     */
    void onUserChangeGame(UserChangeGameEvent event);
}
