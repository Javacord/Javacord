package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.ServerChannelBuilder;
import org.javacord.api.entity.permission.Permissions;

/**
 * This class is internally used by the {@link ServerChannelBuilder} to create server channels.
 * You usually don't want to interact with this object.
 */
public interface ServerChannelBuilderDelegate {

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     */
    void setName(String name);

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
}
