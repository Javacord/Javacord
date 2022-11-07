package org.javacord.api.listener.server.role;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.role.UserRoleRemoveEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to users being removed from a role.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MEMBERS})
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
