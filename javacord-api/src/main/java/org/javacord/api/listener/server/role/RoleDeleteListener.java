package org.javacord.api.listener.server.role;

import org.javacord.api.event.server.role.RoleDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to role deletions.
 */
@FunctionalInterface
public interface RoleDeleteListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role is deleted.
     *
     * @param event The event.
     */
    void onRoleDelete(RoleDeleteEvent event);
}
