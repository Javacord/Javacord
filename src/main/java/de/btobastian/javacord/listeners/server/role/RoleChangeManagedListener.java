package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeManagedEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to role managed flag changes.
 */
@FunctionalInterface
public interface RoleChangeManagedListener extends ServerAttachableListener, RoleAttachableListener,
                                                   GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's managed flag changes.
     *
     * @param event The event.
     */
    void onRoleChangeManaged(RoleChangeManagedEvent event);
}
