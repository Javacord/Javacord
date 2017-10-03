package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangePermissionsEvent;

/**
 * This listener listens to role permission changes.
 */
@FunctionalInterface
public interface RoleChangePermissionsListener {

    /**
     * This method is called every time a role's permissions changes.
     *
     * @param event The event.
     */
    void onRoleChangePermissions(RoleChangePermissionsEvent event);
}
