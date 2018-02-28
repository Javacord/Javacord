package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleDeleteEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
