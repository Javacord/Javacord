package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.RegularServerChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.util.internal.DelegateFactory;

public class RegularServerChannelUpdater<T extends RegularServerChannelUpdater<T>> extends ServerChannelUpdater<T> {

    /**
     * The regular server channel updater delegate used by this instance.
     */
    protected final RegularServerChannelUpdaterDelegate regularServerChannelUpdaterDelegate;

    /**
     * Creates a new regular server channel updater.
     */
    protected RegularServerChannelUpdater(RegularServerChannelUpdaterDelegate regularServerChannelUpdaterDelegate) {
        super(regularServerChannelUpdaterDelegate);
        this.regularServerChannelUpdaterDelegate = regularServerChannelUpdaterDelegate;
    }

    /**
     * Creates a new regular server channel updater.
     * @param channel The channel to update.
     */
    public RegularServerChannelUpdater(RegularServerChannel channel) {
        this(DelegateFactory.createRegularServerChannelUpdaterDelegate(channel));
    }

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link RegularServerChannel#getRawPosition()} instead of
     *                    {@link RegularServerChannel#getPosition()}!
     * @return The current instance in order to chain call methods.
     */
    public T setRawPosition(int rawPosition) {
        regularServerChannelUpdaterDelegate.setRawPosition(rawPosition);
        return (T) this;
    }

    /**
     * Adds a permission overwrite for the given entity.
     *
     * @param <U>            The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permissions should be overwritten.
     * @param permissions    The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    public <U extends Permissionable & DiscordEntity> T addPermissionOverwrite(
            U permissionable, Permissions permissions) {
        regularServerChannelUpdaterDelegate.addPermissionOverwrite(permissionable, permissions);
        return (T) this;
    }

    /**
     * Removes a permission overwrite for the given entity.
     *
     * @param <U>            The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    public <U extends Permissionable & DiscordEntity> T removePermissionOverwrite(U permissionable) {
        regularServerChannelUpdaterDelegate.removePermissionOverwrite(permissionable);
        return (T) this;
    }
}
