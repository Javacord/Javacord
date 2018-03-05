package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleChangePermissionsEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to role permission changes.
 */
@FunctionalInterface
public interface RoleChangePermissionsListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's permissions changes.
     *
     * @param event The event.
     */
    void onRoleChangePermissions(RoleChangePermissionsEvent event);
}
