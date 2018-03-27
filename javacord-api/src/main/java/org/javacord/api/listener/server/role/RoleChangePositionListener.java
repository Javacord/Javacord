package org.javacord.api.listener.server.role;

import org.javacord.api.event.server.role.RoleChangePositionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
