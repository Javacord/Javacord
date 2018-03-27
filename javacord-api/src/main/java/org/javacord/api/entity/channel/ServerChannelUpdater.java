package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server channels.
 */
public class ServerChannelUpdater {

    /**
     * The server channel delegate used by this instance.
     */
    private final ServerChannelUpdaterDelegate delegate;

    /**
     * Creates a new server channel updater without delegate.
     */
    protected ServerChannelUpdater() {
        delegate = null;
    }

    /**
     * Creates a new server channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerChannelUpdater(ServerChannel channel) {
        delegate = DelegateFactory.createServerChannelUpdaterDelegate(channel);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link ServerChannel#getRawPosition()} instead of {@link ServerChannel#getPosition()}!
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater setRawPosition(int rawPosition) {
        delegate.setRawPosition(rawPosition);
        return this;
    }

    /**
     * Adds a permission overwrite for the given user.
     *
     * @param user The user whose permissions should be overwritten.
     * @param permissions The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater addPermissionOverwrite(User user, Permissions permissions) {
        delegate.addPermissionOverwrite(user, permissions);
        return this;
    }

    /**
     * Adds a permission overwrite for the given role.
     *
     * @param role The role which permissions should be overwritten.
     * @param permissions The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater addPermissionOverwrite(Role role, Permissions permissions) {
        delegate.addPermissionOverwrite(role, permissions);
        return this;
    }

    /**
     * Removes a permission overwrite for the given user.
     *
     * @param user The user whose permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater removePermissionOverwrite(User user) {
        delegate.removePermissionOverwrite(user);
        return this;
    }

    /**
     * Removes a permission overwrite for the given role.
     *
     * @param role The role which permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater removePermissionOverwrite(Role role) {
        delegate.removePermissionOverwrite(role);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
