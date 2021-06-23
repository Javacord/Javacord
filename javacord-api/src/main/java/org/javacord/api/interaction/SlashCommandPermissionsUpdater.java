package org.javacord.api.interaction;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.SlashCommandPermissionsUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SlashCommandPermissionsUpdater {

    private final SlashCommandPermissionsUpdaterDelegate delegate;

    /**
     * Creates a new SlashCommandPermissionsUpdater for the given Server.
     *
     * @param server The server where the slash command permissions should be applied on.
     */
    public SlashCommandPermissionsUpdater(Server server) {
        delegate = DelegateFactory.createSlashCommandPermissionsUpdaterDelegate(server);
    }

    /**
     * Sets the permissions of the slash command updater.
     *
     * @param permissions The permissions.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsUpdater setPermissions(List<SlashCommandPermissions> permissions) {
        delegate.setPermissions(permissions);
        return this;
    }

    /**
     * Add the permission to the slash command updater.
     *
     * @param permission The permission.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsUpdater addPermission(SlashCommandPermissions permission) {
        delegate.addPermission(permission);
        return this;
    }

    /**
     * Add the permission to the slash command updater.
     *
     * @param id The ID of the entity which should be updated.
     * @param type The type of the ID which may be a user or a role.
     * @param permission True if allowed, otherwise false.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsUpdater addPermission(
            long id, SlashCommandPermissionType type, boolean permission) {
        delegate.addPermission(SlashCommandPermissions.create(id, type, permission));
        return this;
    }

    /**
     * Adds the permissions to the slash command updater.
     *
     * @param permissions The permissions.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandPermissionsUpdater addPermissions(List<SlashCommandPermissions> permissions) {
        delegate.addPermissions(permissions);
        return this;
    }

    /**
     * Updates the command with the set permissions. When updating multiple slash command permissions at once,
     * prefer using {@link org.javacord.api.DiscordApi#batchUpdateSlashCommandPermissions(Server, List)} as this
     * requires only 1 request.
     *
     * @param commandId The ID of the command which should be updated.
     * @return The updated server slash command permissions.
     */
    public CompletableFuture<ServerSlashCommandPermissions> update(long commandId) {
        return delegate.update(commandId);
    }

}
