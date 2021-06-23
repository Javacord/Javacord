package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.SlashCommandPermissionType;
import org.javacord.api.interaction.SlashCommandPermissions;

public interface SlashCommandPermissionsBuilderDelegate {

    /**
     * Sets the ID of the slash command permissions.
     *
     * @param id The id.
     */
    void setId(long id);

    /**
     * Sets the type of the slash command permissions.
     *
     * @param type The type.
     */
    void setType(SlashCommandPermissionType type);

    /**
     * Sets the permissions of the slash command option whether the command should be enabled or disabled.
     *
     * @param permission The permission.
     */
    void setPermission(boolean permission);

    /**
     * Builds the slash command permissions.
     *
     * @return A new instance of the SlashCommandPermissions.
     */
    SlashCommandPermissions build();
}
