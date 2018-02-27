package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleChangeNameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
