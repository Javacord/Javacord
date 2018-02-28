package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleChangePositionEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
