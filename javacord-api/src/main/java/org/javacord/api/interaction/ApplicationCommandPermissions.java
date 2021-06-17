package org.javacord.api.interaction;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

public interface ApplicationCommandPermissions {

    /**
     * Gets the ID of the affected {@link ApplicationCommandPermissionType}.
     *
     * @return The ID..
     */
    long getId();

    /**
     * Gets the type of this application commands permissions which may be a {@link User} or a {@link Role}.
     *
     * @return The type of this application command permissions.
     */
    ApplicationCommandPermissionType getType();

    /**
     * Whether this permission is enabled or disabled for the this application command permissions.
     *
     * @return True if the command is enabled for this user or role. Otherwise false.
     */
    boolean getPermission();

    /**
     * Creates an application command permissions which can be used by {@link ApplicationCommandPermissionsUpdater}
     * to update the permission.
     *
     * @param id The id of the role or user which should be updated.
     * @param type The type this ID belong to.
     * @param permission True if the command should be enabled.
     * @return The built ApplicationCommandPermissions.
     */
    static ApplicationCommandPermissions create(long id, ApplicationCommandPermissionType type, boolean permission) {
        return new ApplicationCommandPermissionsBuilder()
                .setId(id)
                .setType(type)
                .setPermission(permission)
                .build();
    }
}
