package de.btobastian.javacord.listener.server.role;

import de.btobastian.javacord.event.server.role.RoleChangeMentionableEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
