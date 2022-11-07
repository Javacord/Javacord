package org.javacord.api.listener.server.role;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.role.RoleChangeNameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to role name changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface RoleChangeNameListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's name changes.
     *
     * @param event The event.
     */
    void onRoleChangeName(RoleChangeNameEvent event);
}
