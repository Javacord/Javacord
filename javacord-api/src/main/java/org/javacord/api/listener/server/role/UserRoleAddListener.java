package org.javacord.api.listener.server.role;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.role.UserRoleAddEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to users being added to a role.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MEMBERS})
public interface UserRoleAddListener extends ServerAttachableListener, UserAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user got added to a role.
     *
     * @param event The event.
     */
    void onUserRoleAdd(UserRoleAddEvent event);
}
