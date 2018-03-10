package org.javacord.entity.channel;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;

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
     * Adds a permission overwrite for the given user.
     *
     * @param user The user whose permissions should be overwritten.
     * @param permissions The permission overwrites.
     */
    void addPermissionOverwrite(User user, Permissions permissions);

    /**
     * Adds a permission overwrite for the given role.
     *
     * @param role The role which permissions should be overwritten.
     * @param permissions The permission overwrites.
     */
    void addPermissionOverwrite(Role role, Permissions permissions);

    /**
     * Removes a permission overwrite for the given user.
     *
     * @param user The user whose permission overwrite should be removed.
     */
    void removePermissionOverwrite(User user);

    /**
     * Removes a permission overwrite for the given role.
     *
     * @param role The role which permission overwrite should be removed.
     */
    void removePermissionOverwrite(Role role);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
