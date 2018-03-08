package org.javacord.event.server.role.impl;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.event.server.role.RoleChangePermissionsEvent;

/**
 * The implementation of {@link RoleChangePermissionsEvent}.
 */
public class ImplRoleChangePermissionsEvent extends ImplRoleEvent implements RoleChangePermissionsEvent {

    /**
     * The new permissions of the role.
     */
    private final Permissions newPermissions;

    /**
     * The old permissions of the role.
     */
    private final Permissions oldPermissions;

    /**
     * Creates a new role change color event.
     *
     * @param role The role of the event.
     * @param oldPermissions The old permissions of the role.
     * @param newPermissions The new permissions of the role.
     */
    public ImplRoleChangePermissionsEvent(Role role, Permissions newPermissions, Permissions oldPermissions) {
        super(role);
        this.newPermissions = newPermissions;
        this.oldPermissions = oldPermissions;
    }

    @Override
    public Permissions getNewPermissions() {
        return newPermissions;
    }

    @Override
    public Permissions getOldPermissions() {
        return oldPermissions;
    }
}
