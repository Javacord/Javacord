package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeNameEvent;

/**
 * This listener listens to role name changes.
 */
@FunctionalInterface
public interface RoleChangeNameListener {

    /**
     * This method is called every time a roles name changes.
     *
     * @param event The event.
     */
    void onRoleChangeName(RoleChangeNameEvent event);
}
