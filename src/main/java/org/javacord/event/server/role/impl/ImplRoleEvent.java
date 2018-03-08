package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.event.server.impl.ImplServerEvent;
import org.javacord.event.server.role.RoleEvent;

/**
 * The implementation of {@link RoleEvent}.
 */
public abstract class ImplRoleEvent extends ImplServerEvent implements RoleEvent {

    /**
     * The role of the event
     */
    private final Role role;

    /**
     * Creates a new role event.
     *
     * @param role The role of the event.
     */
    public ImplRoleEvent(Role role) {
        super(role.getServer());
        this.role = role;
    }

    @Override
    public Role getRole() {
        return role;
    }
}
