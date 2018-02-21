package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangePermissionsEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
