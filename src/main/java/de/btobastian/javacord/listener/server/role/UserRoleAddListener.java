package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.UserRoleAddEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to users being added to a role.
 */
@FunctionalInterface
public interface UserRoleAddListener extends ServerAttachableListener, UserAttachableListener, RoleAttachableListener,
                                             GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user got added to a role.
     *
     * @param event The event.
     */
    void onUserRoleAdd(UserRoleAddEvent event);
}
