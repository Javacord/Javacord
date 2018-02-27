package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleChangeHoistEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to role hoist changes.
 */
@FunctionalInterface
public interface RoleChangeHoistListener extends ServerAttachableListener, RoleAttachableListener,
                                                 GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's hoist changes.
     *
     * @param event The event.
     */
    void onRoleChangeHoist(RoleChangeHoistEvent event);
}
