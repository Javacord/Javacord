package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeColorEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
