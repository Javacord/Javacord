package org.javacord.listener.server.role;

import org.javacord.event.server.role.RoleChangeMentionableEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.role.RoleChangeMentionableEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to role mentionable changes.
 */
@FunctionalInterface
public interface RoleChangeMentionableListener extends ServerAttachableListener, RoleAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a role's mentionable flag changes.
     *
     * @param event The event.
     */
    void onRoleChangeMentionable(RoleChangeMentionableEvent event);
}
