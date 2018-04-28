package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleCreateEvent;

/**
 * The implementation of {@link RoleCreateEvent}.
 */
public class RoleCreateEventImpl extends RoleEventImpl implements RoleCreateEvent {

    /**
     * Creates a new role create event.
     *
     * @param role The role of the event.
     */
    public RoleCreateEventImpl(Role role) {
        super(role);
    }

}
