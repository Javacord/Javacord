package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangePositionEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to role position changes.
 */
@FunctionalInterface
public interface RoleChangePositionListener extends ServerAttachableListener, RoleAttachableListener,
                                                    GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's position changes.
     *
     * @param event The event.
     */
    void onRoleChangePosition(RoleChangePositionEvent event);
}
