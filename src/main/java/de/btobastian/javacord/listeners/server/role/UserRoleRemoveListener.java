package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.UserRoleRemoveEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

/**
 * This listener listens to users being removed from a role.
 */
@FunctionalInterface
public interface UserRoleRemoveListener extends ServerAttachableListener, UserAttachableListener,
                                                RoleAttachableListener, GloballyAttachableListener,
                                                ObjectAttachableListener {

    /**
     * This method is called every time a user got removed from a role.
     *
     * @param event The event.
     */
    void onUserRoleRemove(UserRoleRemoveEvent event);
}
