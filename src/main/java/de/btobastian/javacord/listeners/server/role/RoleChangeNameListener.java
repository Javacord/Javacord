package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeNameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
