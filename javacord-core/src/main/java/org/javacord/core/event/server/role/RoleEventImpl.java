package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link RoleEvent}.
 */
public abstract class RoleEventImpl extends ServerEventImpl implements RoleEvent {

    /**
     * The role of the event.
     */
    private final Role role;

    /**
     * Creates a new role event.
     *
     * @param role The role of the event.
     */
    public RoleEventImpl(Role role) {
        super(role.getServer());
        this.role = role;
    }

    @Override
    public Role getRole() {
        return role;
    }
}
