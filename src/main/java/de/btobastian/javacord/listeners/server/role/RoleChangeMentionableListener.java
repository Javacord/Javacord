package de.btobastian.javacord.listeners.server.role;

import de.btobastian.javacord.events.server.role.RoleChangeMentionableEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;

/**
 * This listener listens to role mentionable changes.
 */
@FunctionalInterface
public interface RoleChangeMentionableListener extends GloballyAttachableListener {

    /**
     * This method is called every time a role's mentionable flag changes.
     *
     * @param event The event.
     */
    void onRoleChangeMentionable(RoleChangeMentionableEvent event);
}
