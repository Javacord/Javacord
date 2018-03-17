package org.javacord.api.event.channel.server;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.Optional;

/**
 * A server channel change overwritten permissions event.
 */
public interface ServerChannelChangeOverwrittenPermissionsEvent extends ServerChannelEvent {

    /**
     * Gets the new overwritten permissions.
     *
     * @return The new permissions.
     */
    Permissions getNewPermissions();

    /**
     * Gets the old overwritten permissions.
     *
     * @return The old permissions.
     */
    Permissions getOldPermissions();

    /**
     * Gets the entity which permissions were changed.
     * The entity is a user or a role.
     *
     * @return The entity which permissions were changed.
     */
    DiscordEntity getEntity();

    /**
     * Gets the user which permissions were changed.
     * Only present if the entity is a user!
     *
     * @return The user which permissions were changed.
     */
    Optional<User> getUser();

    /**
     * Gets the role which permissions were changed.
     * Only present if the entity is a role!
     *
     * @return The role which permissions were changed.
     */
    Optional<Role> getRole();

}
