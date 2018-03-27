package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleChangeNameEvent;

/**
 * The implementation of {@link RoleChangeNameEvent}.
 */
public class RoleChangeNameEventImpl extends RoleEventImpl implements RoleChangeNameEvent {

    /**
     * The new name of the role.
     */
    private final String newName;

    /**
     * The old name of the role.
     */
    private final String oldName;

    /**
     * Creates a new role change name.
     *
     * @param role The role of the event.
     * @param newName The new name of the role.
     * @param oldName The old name of the role.
     */
    public RoleChangeNameEventImpl(Role role, String newName, String oldName) {
        super(role);
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }

    @Override
    public String getNewName() {
        return newName;
    }
}
