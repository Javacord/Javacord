package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.UserRoleAddEvent;

/**
 * This listener listens to users being added to a role.
 */
@FunctionalInterface
public interface UserRoleAddListener {

    /**
     * This method is called every time a user got added to a role.
     *
     * @param event The event.
     */
    void onUserRoleAdd(UserRoleAddEvent event);
}
