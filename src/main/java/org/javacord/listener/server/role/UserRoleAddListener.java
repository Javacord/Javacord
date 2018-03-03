package org.javacord.listener.server.role;

import org.javacord.event.server.role.UserRoleAddEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.server.role.UserRoleAddEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
