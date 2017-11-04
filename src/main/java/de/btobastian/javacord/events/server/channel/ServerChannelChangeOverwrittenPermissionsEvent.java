package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.Optional;

/**
 * A server channel change overwritten permissions event.
 */
public class ServerChannelChangeOverwrittenPermissionsEvent extends ServerChannelEvent {

    /**
     * The new overwritten permissions.
     */
    private final Permissions newPermissions;

    /**
     * The old overwritten permissions.
     */
    private final Permissions oldPermissions;

    /**
     * The entity which permissions got changed.
     */
    private final DiscordEntity entity;

    /**
     * Creates a new server channel create event.
     *
     * @param channel The channel of the event.
     * @param newPermissions The new overwritten permissions.
     * @param oldPermissions The old overwritten permissions.
     * @param entity The entity which permissions got changed.
     */
    public ServerChannelChangeOverwrittenPermissionsEvent(
            ServerChannel channel, Permissions newPermissions, Permissions oldPermissions, DiscordEntity entity) {
        super(channel);
        this.newPermissions = newPermissions;
        this.oldPermissions = oldPermissions;
        this.entity = entity;
    }

    /**
     * Gets the new overwritten permissions.
     *
     * @return The new permissions.
     */
    public Permissions getNewPermissions() {
        return newPermissions;
    }

    /**
     * Gets the old overwritten permissions.
     *
     * @return The old permissions.
     */
    public Permissions getOldPermissions() {
        return oldPermissions;
    }

    /**
     * Gets the entity which permissions were changed.
     * The entity is a user or a role.
     *
     * @return The entity which permissions were changed.
     */
    public DiscordEntity getEntity() {
        return entity;
    }

    /**
     * Gets the user which permissions were changed.
     * Only present if the entity is a user!
     *
     * @return The user which permissions were changed.
     */
    public Optional<User> getUser() {
        if (entity instanceof User) {
            return Optional.of((User) entity);
        }
        return Optional.empty();
    }

    /**
     * Gets the role which permissions were changed.
     * Only present if the entity is a role!
     *
     * @return The role which permissions were changed.
     */
    public Optional<Role> getRole() {
        if (entity instanceof Role) {
            return Optional.of((Role) entity);
        }
        return Optional.empty();
    }
}
