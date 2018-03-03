package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleChangePositionEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.role.RoleChangePositionEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
