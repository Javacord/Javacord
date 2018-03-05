package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to role name changes.
 */
@FunctionalInterface
public interface RoleChangeNameListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's name changes.
     *
     * @param event The event.
     */
    void onRoleChangeName(RoleChangeNameEvent event);
}
