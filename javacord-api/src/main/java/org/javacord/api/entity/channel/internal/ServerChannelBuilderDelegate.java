package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.ChannelCategory;
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
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     */
    void setCategory(ChannelCategory category);

    /**
     * Adds a permission overwrite for the given entity.
     *
     * @param permissionable The entity whose permissions should be overwritten.
     * @param permissions The permission overwrites.
     */
    void addPermissionOverwrite(Permissionable permissionable, Permissions permissions);

    /**
     * Removes a permission overwrite for the given entity.
     *
     * @param permissionable The entity whose permission overwrite should be removed.
     */
    void removePermissionOverwrite(Permissionable permissionable);
}
