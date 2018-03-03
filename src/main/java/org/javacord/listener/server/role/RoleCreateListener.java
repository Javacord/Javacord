package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.role.RoleCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to role creations.
 */
@FunctionalInterface
public interface RoleCreateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a role is created.
     *
     * @param event The event.
     */
    void onRoleCreate(RoleCreateEvent event);
}
