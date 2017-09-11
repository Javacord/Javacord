package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.UserRoleAddEvent;

/**
 * This listener listens to role hoist changes.
 */
@FunctionalInterface
public interface UserRoleAddListener {

    /**
     * This method is called every time the role is added to a user.
     *
     * @param event The event.
     */
    void onUserRoleAdd(UserRoleAddEvent event);
}
