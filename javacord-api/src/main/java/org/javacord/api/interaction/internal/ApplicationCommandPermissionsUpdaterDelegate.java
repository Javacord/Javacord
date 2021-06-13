package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.ApplicationCommandPermissions;
import org.javacord.api.interaction.ServerApplicationCommandPermissions;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandPermissionsUpdaterDelegate {

    /**
     * Sets the permissions of the application command updater.
     *
     * @param permissions The permissions.
     */
    void setPermissions(List<ApplicationCommandPermissions> permissions);

    /**
     * Add the permissions to the application command updater.
     *
     * @param permissions The permissions.
     */
    void addPermissions(List<ApplicationCommandPermissions> permissions);

    /**
     * Adds the permission to the application command updater.
     *
     * @param permission The permission.
     */
    void addPermission(ApplicationCommandPermissions permission);

    /**
     * Updates the command with the set permissions.
     *
     * @param commandId The ID of the command which should be updated.
     * @return The updated server application command permissions.
     */
    CompletableFuture<ServerApplicationCommandPermissions> update(long commandId);
}
