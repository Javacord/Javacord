package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleChangePermissionsEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
