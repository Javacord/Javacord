package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangePositionEvent;

/**
 * This listener listens to role position changes.
 */
@FunctionalInterface
public interface RoleChangePositionListener {

    /**
     * This method is called every time a role's position changes.
     *
     * @param event The event.
     */
    void onRoleChangePosition(RoleChangePositionEvent event);
}
