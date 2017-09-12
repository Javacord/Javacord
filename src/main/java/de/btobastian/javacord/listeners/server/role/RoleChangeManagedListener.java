package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeManagedEvent;

/**
 * This listener listens to role managed flag changes.
 */
@FunctionalInterface
public interface RoleChangeManagedListener {

    /**
     * This method is called every time a roles managed flag changes.
     *
     * @param event The event.
     */
    void onRoleChangeManaged(RoleChangeManagedEvent event);
}
