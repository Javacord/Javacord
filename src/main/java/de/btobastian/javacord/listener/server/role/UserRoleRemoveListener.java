package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.UserRoleRemoveEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
