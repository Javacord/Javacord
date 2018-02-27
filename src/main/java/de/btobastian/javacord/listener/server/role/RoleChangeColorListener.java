package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleChangeColorEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
