package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.ApplicationCommandPermissionsBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class ApplicationCommandPermissionsBuilder {

    private final ApplicationCommandPermissionsBuilderDelegate delegate =
            DelegateFactory.createApplicationCommandPermissionsBuilderDelegate();

    /**
     * Sets the ID of the application command permissions.
     *
     * @param id The id.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsBuilder setId(long id) {
        delegate.setId(id);
        return this;
    }

    /**
     * Sets the type of the application command permissions.
     *
     * @param type The type.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsBuilder setType(ApplicationCommandPermissionType type) {
        delegate.setType(type);
        return this;
    }

    /**
     * Sets the permissions of the application command option whether the command should be enabled or disabled.
     *
     * @param permission The permission.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsBuilder setPermission(boolean permission) {
        delegate.setPermission(permission);
        return this;
    }

    /**
     * Builds the application command permissions.
     *
     * @return A new instance of the ApplicationCommandPermissions.
     */
    public ApplicationCommandPermissions build() {
        return delegate.build();
    }

}
