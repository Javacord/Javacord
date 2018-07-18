package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerChannelUpdater;
import org.javacord.api.entity.permission.Permissions;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerChannelUpdater} to update server channels.
 * You usually don't want to interact with this object.
 */
public interface ServerChannelUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     */
    void setName(String name);

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link ServerChannel#getRawPosition()} instead of {@link ServerChannel#getPosition()}!
     */
    void setRawPosition(int rawPosition);

    /**
     * Adds a permission overwrite for the given entity.
     *
     * @param <T> The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permissions should be overwritten.
     * @param permissions The permission overwrites.
     */
    <T extends Permissionable & DiscordEntity> void addPermissionOverwrite(T permissionable, Permissions permissions);

    /**
     * Removes a permission overwrite for the given entity.
     *
     * @param <T> The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permission overwrite should be removed.
     */
    <T extends Permissionable & DiscordEntity> void removePermissionOverwrite(T permissionable);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
