package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.UserRoleRemoveEvent;

/**
 * This listener listens to users being removed from a role.
 */
@FunctionalInterface
public interface UserRoleRemoveListener {

    /**
     * This method is called every time a user got removed from a role.
     *
     * @param event The event.
     */
    void onUserRoleRemove(UserRoleRemoveEvent event);
}
