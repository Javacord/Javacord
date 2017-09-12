package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleDeleteEvent;

/**
 * This listener listens to role deletions.
 */
@FunctionalInterface
public interface RoleDeleteListener {

    /**
     * This method is called every time a role got deleted.
     *
     * @param event The event.
     */
    void onRoleDelete(RoleDeleteEvent event);
}
