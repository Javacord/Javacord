package org.javacord.api.interaction;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

public interface SlashCommandPermissions {

    /**
     * Gets the ID of the affected {@link SlashCommandPermissionType}.
     *
     * @return The ID..
     */
    long getId();

    /**
     * Gets the type of this slash commands permissions which may be a {@link User} or a {@link Role}.
     *
     * @return The type of this slash command permissions.
     */
    SlashCommandPermissionType getType();

    /**
     * Whether this permission is enabled or disabled for the this slash command permissions.
     *
     * @return True if the command is enabled for this user or role. Otherwise false.
     */
    boolean getPermission();

    /**
     * Creates an slash command permissions which can be used by {@link SlashCommandPermissionsUpdater}
     * to update the permission.
     *
     * @param id The id of the role or user which should be updated.
     * @param type The type this ID belong to.
     * @param permission True if the command should be enabled.
     * @return The built SlashCommandPermissions.
     */
    static SlashCommandPermissions create(long id, SlashCommandPermissionType type, boolean permission) {
        return new SlashCommandPermissionsBuilder()
                .setId(id)
                .setType(type)
                .setPermission(permission)
                .build();
    }
}
