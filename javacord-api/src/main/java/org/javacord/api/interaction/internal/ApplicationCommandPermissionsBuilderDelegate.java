package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.ApplicationCommandPermissionType;
import org.javacord.api.interaction.ApplicationCommandPermissions;

public interface ApplicationCommandPermissionsBuilderDelegate {

    /**
     * Sets the ID of the application command permissions.
     *
     * @param id The id.
     */
    void setId(long id);

    /**
     * Sets the type of the application command permissions.
     *
     * @param type The type.
     */
    void setType(ApplicationCommandPermissionType type);

    /**
     * Sets the permissions of the application command option whether the command should be enabled or disabled.
     *
     * @param permission The permission.
     */
    void setPermission(boolean permission);

    /**
     * Builds the application command permissions.
     *
     * @return A new instance of the ApplicationCommandPermissions.
     */
    ApplicationCommandPermissions build();
}
