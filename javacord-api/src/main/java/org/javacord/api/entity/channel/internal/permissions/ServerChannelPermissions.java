package org.javacord.api.entity.channel.internal.permissions;

import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;

public interface ServerChannelPermissions {

    /**
     * Gets the channel to check the permissions on.
     * This can be different depending on the type of the channel. For example a {@link ServerThreadChannel} will
     * return the (parent)channel the thread was created in because threads mostly inherit their permissions.
     *
     * @return The channel to check the permissions on.
     */
    RegularServerChannel getPermissionableChannel();

}
