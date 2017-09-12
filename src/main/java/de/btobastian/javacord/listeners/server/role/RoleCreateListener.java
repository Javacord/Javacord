package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleCreateEvent;

/**
 * This listener listens to role creations.
 */
@FunctionalInterface
public interface RoleCreateListener {

    /**
     * This method is called every time a role got created.
     *
     * @param event The event.
     */
    void onRoleCreate(RoleCreateEvent event);
}
