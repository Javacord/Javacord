package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleDeleteEvent;

/**
 * The implementation of {@link RoleDeleteEvent}.
 */
public class RoleDeleteEventImpl extends RoleEventImpl implements RoleDeleteEvent {

    /**
     * Creates a new role delete event.
     *
     * @param role The role of the event.
     */
    public RoleDeleteEventImpl(Role role) {
        super(role);
    }

}
