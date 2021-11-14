package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.RegularServerChannelBuilderDelegate;
import org.javacord.api.entity.permission.Permissions;

/**
 * This class is used to create new regular server channels.
 */
public class RegularServerChannelBuilder<T> extends ServerChannelBuilder<T> {

    /**
     * The regular server channel builder delegate used by this instance.
     */
    protected final RegularServerChannelBuilderDelegate delegate;

    /**
     * Creates a new regular server channel builder.
     */
    protected RegularServerChannelBuilder(Class<T> myClass, RegularServerChannelBuilderDelegate delegate) {
        super(myClass, delegate);
        this.delegate = delegate;
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
        delegate.setRawPosition(rawPosition);
        return myClass.cast(this);
    }

    /**
     * Adds a permission overwrite for the given entity.
     *
     * @param <U>            The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permissions should be overwritten.
     * @param permissions    The permission overwrites.
     * @return The current instance in order to chain call methods.
     */
    public <U extends Permissionable & DiscordEntity> T addPermissionOverwrite(U permissionable,
                                                                               Permissions permissions) {
        delegate.addPermissionOverwrite(permissionable, permissions);
        return myClass.cast(this);
    }

    /**
     * Removes a permission overwrite for the given entity.
     *
     * @param <U>            The type of entity to hold the permission, usually <code>User</code> or <code>Role</code>
     * @param permissionable The entity whose permission overwrite should be removed.
     * @return The current instance in order to chain call methods.
     */
    public <U extends Permissionable & DiscordEntity> T removePermissionOverwrite(U permissionable) {
        delegate.removePermissionOverwrite(permissionable);
        return myClass.cast(this);
    }

}
