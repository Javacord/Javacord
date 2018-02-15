package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.UserRoleAddEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
