package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeStatusEvent;

/**
 * This listener listens to user status changes.
 */
@FunctionalInterface
public interface UserChangeStatusListener {

    /**
     * This method is called every time a user changed their status.
     *
     * @param event The event.
     */
    void onUserChangeStatus(UserChangeStatusEvent event);
}
