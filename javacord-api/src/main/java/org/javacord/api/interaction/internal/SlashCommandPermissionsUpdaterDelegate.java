package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.ServerSlashCommandPermissions;
import org.javacord.api.interaction.SlashCommandPermissions;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SlashCommandPermissionsUpdaterDelegate {

    /**
     * Sets the permissions of the slash command updater.
     *
     * @param permissions The permissions.
     */
    void setPermissions(List<SlashCommandPermissions> permissions);

    /**
     * Add the permissions to the slash command updater.
     *
     * @param permissions The permissions.
     */
    void addPermissions(List<SlashCommandPermissions> permissions);

    /**
     * Adds the permission to the slash command updater.
     *
     * @param permission The permission.
     */
    void addPermission(SlashCommandPermissions permission);

    /**
     * Updates the command with the set permissions.
     *
     * @param commandId The ID of the command which should be updated.
     * @return The updated server slash command permissions.
     */
    CompletableFuture<ServerSlashCommandPermissions> update(long commandId);
}
