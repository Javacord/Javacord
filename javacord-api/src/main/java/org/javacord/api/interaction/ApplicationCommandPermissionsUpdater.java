package org.javacord.api.interaction;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandPermissionsUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationCommandPermissionsUpdater {

    private final ApplicationCommandPermissionsUpdaterDelegate delegate;

    /**
     * Creates a new ApplicationCommandPermissionsUpdater for the given Server.
     *
     * @param server The server where the application command permissions should be applied on.
     */
    public ApplicationCommandPermissionsUpdater(Server server) {
        delegate = DelegateFactory.createApplicationCommandPermissionsUpdaterDelegate(server);
    }

    /**
     * Sets the permissions of the application command updater.
     *
     * @param permissions The permissions.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsUpdater setPermissions(List<ApplicationCommandPermissions> permissions) {
        delegate.setPermissions(permissions);
        return this;
    }

    /**
     * Add the permission to the application command updater.
     *
     * @param permission The permission.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsUpdater addPermission(ApplicationCommandPermissions permission) {
        delegate.addPermission(permission);
        return this;
    }

    /**
     * Add the permission to the application command updater.
     *
     * @param id The ID of the entity which should be updated.
     * @param type The type of the ID which may be a user or a role.
     * @param permission True if allowed, otherwise false.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsUpdater addPermission(
            long id, ApplicationCommandPermissionType type, boolean permission) {
        delegate.addPermission(ApplicationCommandPermissions.create(id, type, permission));
        return this;
    }

    /**
     * Adds the permissions to the application command updater.
     *
     * @param permissions The permissions.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandPermissionsUpdater addPermissions(List<ApplicationCommandPermissions> permissions) {
        delegate.addPermissions(permissions);
        return this;
    }

    /**
     * Updates the command with the set permissions. When updating multiple application command permissions at once,
     * prefer using {@link org.javacord.api.DiscordApi#batchUpdateApplicationCommandPermissions(Server, List)} as this
     * requires only 1 request.
     *
     * @param commandId The ID of the command which should be updated.
     * @return The updated server application command permissions.
     */
    public CompletableFuture<ServerApplicationCommandPermissions> update(long commandId) {
        return delegate.update(commandId);
    }

}
