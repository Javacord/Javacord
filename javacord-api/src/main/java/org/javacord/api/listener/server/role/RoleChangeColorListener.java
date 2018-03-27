package org.javacord.api.listener.server.role;

import org.javacord.api.event.server.role.RoleChangeColorEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to role color changes.
 */
@FunctionalInterface
public interface RoleChangeColorListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's color changes.
     *
     * @param event The event.
     */
    void onRoleChangeColor(RoleChangeColorEvent event);
}
