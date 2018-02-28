package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleCreateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
