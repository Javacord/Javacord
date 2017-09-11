package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.UserRoleRemoveEvent;

/**
 * This listener listens to role hoist changes.
 */
@FunctionalInterface
public interface UserRoleRemoveListener {

    /**
     * This method is called every time the role is removed from a user.
     *
     * @param event The event.
     */
    void onUserRoleRemove(UserRoleRemoveEvent event);
}
