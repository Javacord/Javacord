package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.role.RoleDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
