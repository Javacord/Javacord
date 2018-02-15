package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleDeleteEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
