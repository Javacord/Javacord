package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.RegularServerChannelBuilder;
import org.javacord.api.entity.permission.Permissions;

/**
 * This class is internally used by the {@link RegularServerChannelBuilder} to create regular server channels.
 * You usually don't want to interact with this object.
 */
public interface RegularServerChannelBuilderDelegate extends ServerChannelBuilderDelegate {

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link RegularServerChannel#getRawPosition()} instead of
     *                    {@link RegularServerChannel#getPosition()}!
     */
    void setRawPosition(int rawPosition);

    /**
     * Adds a permission overwrite for the given entity.
     *
     * @param <T>            The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permissions should be overwritten.
     * @param permissions    The permission overwrites.
     */
    <T extends Permissionable & DiscordEntity> void addPermissionOverwrite(T permissionable, Permissions permissions);

    /**
     * Removes a permission overwrite for the given entity.
     *
     * @param <T>            The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permission overwrite should be removed.
     */
    <T extends Permissionable & DiscordEntity> void removePermissionOverwrite(T permissionable);
}
