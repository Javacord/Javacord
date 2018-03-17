package org.javacord.api.event.server.role;

import org.javacord.api.entity.permission.Permissions;

/**
 * A role change permissions event.
 */
public interface RoleChangePermissionsEvent extends RoleEvent {

    /**
     * Gets the new permissions of the role.
     *
     * @return The new permissions of the role.
     */
    Permissions getNewPermissions();

    /**
     * Gets the old permissions of the role.
     *
     * @return The old permissions of the role.
     */
    Permissions getOldPermissions();

}
