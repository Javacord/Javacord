package org.javacord.entity.channel;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.entity.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server channels.
 */
public interface ServerChannelUpdater {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater setName(String name);

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link ServerChannel#getRawPosition()} instead of {@link ServerChannel#getPosition()}!
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater setRawPosition(int rawPosition);

    /**
     * Adds a permission overwrite for the given user.
     *
     * @param user The user whose permissions should be overwritten.
     * @param permissions The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater addPermissionOverwrite(User user, Permissions permissions);

    /**
     * Adds a permission overwrite for the given role.
     *
     * @param role The role which permissions should be overwritten.
     * @param permissions The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater addPermissionOverwrite(Role role, Permissions permissions);

    /**
     * Removes a permission overwrite for the given user.
     *
     * @param user The user whose permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater removePermissionOverwrite(User user);

    /**
     * Removes a permission overwrite for the given role.
     *
     * @param role The role which permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    ServerChannelUpdater removePermissionOverwrite(Role role);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
