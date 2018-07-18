package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.ServerChannelBuilderDelegate;
import org.javacord.api.entity.permission.Permissions;

/**
 * This class is used to create new server channels.
 */
public class ServerChannelBuilder {

    /**
     * The server channel delegate used by this instance.
     */
    private final ServerChannelBuilderDelegate delegate;

    /**
     * Creates a new server channel builder without delegate.
     */
    protected ServerChannelBuilder() {
        delegate = null;
    }

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelBuilder setName(String name) {
        delegate.setName(name);
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
    public <T extends Permissionable & DiscordEntity> ServerChannelBuilder addPermissionOverwrite(
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
    public <T extends Permissionable & DiscordEntity> ServerChannelBuilder removePermissionOverwrite(
            T permissionable) {
        delegate.removePermissionOverwrite(permissionable);
        return this;
    }

}
