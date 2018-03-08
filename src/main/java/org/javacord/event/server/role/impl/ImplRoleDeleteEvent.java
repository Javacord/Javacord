package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.event.server.role.RoleDeleteEvent;

/**
 * The implementation of {@link RoleDeleteEvent}.
 */
public class ImplRoleDeleteEvent extends ImplRoleEvent implements RoleDeleteEvent {

    /**
     * Creates a new role delete event.
     *
     * @param role The role of the event.
     */
    public ImplRoleDeleteEvent(Role role) {
        super(role);
    }

}
