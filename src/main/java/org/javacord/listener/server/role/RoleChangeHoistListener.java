package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleChangeHoistEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.role.RoleChangeHoistEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to role hoist changes.
 */
@FunctionalInterface
public interface RoleChangeHoistListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's hoist changes.
     *
     * @param event The event.
     */
    void onRoleChangeHoist(RoleChangeHoistEvent event);
}
