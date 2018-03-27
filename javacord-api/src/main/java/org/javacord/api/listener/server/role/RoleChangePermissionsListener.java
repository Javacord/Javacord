package org.javacord.api.listener.server.role;

import org.javacord.api.event.server.role.RoleChangePermissionsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
