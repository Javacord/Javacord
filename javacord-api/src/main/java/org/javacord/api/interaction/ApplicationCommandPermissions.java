package org.javacord.api.interaction;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.Optional;

public interface ApplicationCommandPermissions {

    /**
     * Gets the ID of the affected {@link ApplicationCommandPermissionType}.
     *
     * @return The ID.
     */
    long getId();

    /**
     * Gets the type of this application commands permissions which may be a {@link User} or a {@link Role}.
     *
     * @return The type of this application command permissions.
     */
    ApplicationCommandPermissionType getType();

    /**
     * Whether this permission is enabled or disabled for the application command permissions.
     *
     * @return True if the command is enabled for this user or role, otherwise false.
     */
    boolean getPermission();

    /**
     * Returns the role that these permissions affect.
     *
     * @return The role that these permissions affect.
     */
    Optional<Role> getRole();

    /**
     * Returns the user that these permissions affect.
     *
     * @return The user that these permissions affect.
     */
    Optional<User> getUser();

    /**
     * Gets the channel that these permissions affect, if it is a single channel.
     *
     * @return The channel that these permissions affect.
     */
    Optional<ServerChannel> getChannel();

    /**
     * Gets the server this command permissions belongs to.
     *
     * @return The server this command permissions belongs to.
     */
    Server getServer();

    /**
     * Gets whether these permissions affect all the server's channels.
     *
     * @return Whether these permissions affect all the server's channels.
     */
    default boolean affectsAllChannels() {
        return getId() == getServer().getId() - 1;
    }

    /**
     * Gets whether these permissions affect the everyone role.
     *
     * @return Whether these permissions affect the everyone role.
     */
    default boolean affectsEveryoneRole() {
        return getId() == getServer().getEveryoneRole().getId();
    }

}
