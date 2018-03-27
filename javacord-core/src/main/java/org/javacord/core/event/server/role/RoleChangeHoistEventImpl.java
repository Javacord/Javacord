package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleChangeHoistEvent;

/**
 * The implementation of {@link RoleChangeHoistEvent}.
 */
public class RoleChangeHoistEventImpl extends RoleEventImpl implements RoleChangeHoistEvent {

    /**
     * The old hoist of the role.
     * (Whether it is pinned separately in the user list or not)
     */
    private final boolean oldHoist;

    /**
     * Creates a new role change hoist event.
     *
     * @param role The role of the event.
     * @param oldHoist The old hoist of the role.
     */
    public RoleChangeHoistEventImpl(Role role, boolean oldHoist) {
        super(role);
        this.oldHoist = oldHoist;
    }

    @Override
    public boolean getOldHoist() {
        return oldHoist;
    }

    @Override
    public boolean getNewHoist() {
        return !oldHoist;
    }
}
