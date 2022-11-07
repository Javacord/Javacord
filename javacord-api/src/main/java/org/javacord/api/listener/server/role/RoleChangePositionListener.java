package org.javacord.api.listener.server.role;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.role.RoleChangePositionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to role position changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface RoleChangePositionListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's position changes.
     *
     * @param event The event.
     */
    void onRoleChangePosition(RoleChangePositionEvent event);
}
