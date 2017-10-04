package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeHoistEvent;

/**
 * This listener listens to role hoist changes.
 */
@FunctionalInterface
public interface RoleChangeHoistListener {

    /**
     * This method is called every time a role's hoist changes.
     *
     * @param event The event.
     */
    void onRoleChangeHoist(RoleChangeHoistEvent event);
}
