package org.javacord.api.listener.server.role;

import org.javacord.api.event.server.role.RoleCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
