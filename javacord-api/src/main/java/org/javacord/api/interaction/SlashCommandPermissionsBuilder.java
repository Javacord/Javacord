package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.SlashCommandPermissionsBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class SlashCommandPermissionsBuilder {

    private final SlashCommandPermissionsBuilderDelegate delegate =
            DelegateFactory.createSlashCommandPermissionsBuilderDelegate();

    /**
     * Sets the ID of the slash command permissions.
     *
     * @param id The id.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsBuilder setId(long id) {
        delegate.setId(id);
        return this;
    }

    /**
     * Sets the type of the slash command permissions.
     *
     * @param type The type.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsBuilder setType(SlashCommandPermissionType type) {
        delegate.setType(type);
        return this;
    }

    /**
     * Sets the permissions of the slash command option whether the command should be enabled or disabled.
     *
     * @param permission The permission.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsBuilder setPermission(boolean permission) {
        delegate.setPermission(permission);
        return this;
    }

    /**
     * Builds the slash command permissions.
     *
     * @return A new instance of the SlashCommandPermissions.
     */
    public SlashCommandPermissions build() {
        return delegate.build();
    }

}
