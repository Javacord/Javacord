package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.ServerChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
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
     * Adds a permission overwrite for the given entity.
     *
     * @param <T> The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permissions should be overwritten.
     * @param permissions The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    public <T extends Permissionable & DiscordEntity> ServerChannelUpdater addPermissionOverwrite(
            T permissionable, Permissions permissions) {
        delegate.addPermissionOverwrite(permissionable, permissions);
        return this;
    }

    /**
     * Removes a permission overwrite for the given entity.
     *
     * @param <T> The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity which permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    public <T extends Permissionable & DiscordEntity> ServerChannelUpdater removePermissionOverwrite(T permissionable) {
        delegate.removePermissionOverwrite(permissionable);
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
