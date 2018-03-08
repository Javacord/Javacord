package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Role;
import org.javacord.event.server.role.RoleChangeNameEvent;

/**
 * The implementation of {@link RoleChangeNameEvent}.
 */
public class ImplRoleChangeNameEvent extends ImplRoleEvent implements RoleChangeNameEvent {

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
    public ImplRoleChangeNameEvent(Role role, String newName, String oldName) {
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
