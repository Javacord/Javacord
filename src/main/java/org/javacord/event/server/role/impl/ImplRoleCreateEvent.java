package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.event.server.role.RoleCreateEvent;

/**
 * The implementation of {@link RoleCreateEvent}.
 */
public class ImplRoleCreateEvent extends ImplRoleEvent implements RoleCreateEvent {

    /**
     * Creates a new role create event
     *
     * @param role The role of the event.
     */
    public ImplRoleCreateEvent(Role role) {
        super(role);
    }

}
