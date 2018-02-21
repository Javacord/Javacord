package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleCreateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
