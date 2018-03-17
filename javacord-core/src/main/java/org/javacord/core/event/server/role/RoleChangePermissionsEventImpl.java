package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleChangePermissionsEvent;

/**
 * The implementation of {@link RoleChangePermissionsEvent}.
 */
public class RoleChangePermissionsEventImpl extends RoleEventImpl implements RoleChangePermissionsEvent {

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
    public RoleChangePermissionsEventImpl(Role role, Permissions newPermissions, Permissions oldPermissions) {
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
