package org.javacord.core.event.channel.server;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerChannelChangeOverwrittenPermissionsEvent}.
 */
public class ServerChannelChangeOverwrittenPermissionsEventImpl extends ServerChannelEventImpl
        implements ServerChannelChangeOverwrittenPermissionsEvent {

    /**
     * The new overwritten permissions.
     */
    private final Permissions newPermissions;

    /**
     * The old overwritten permissions.
     */
    private final Permissions oldPermissions;

    /**
     * The id of the entity.
     */
    private final long entityId;

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
     * @param entityId The id of the entity.
     * @param entity The entity which permissions got changed.
     */
    public ServerChannelChangeOverwrittenPermissionsEventImpl(
            ServerChannel channel, Permissions newPermissions, Permissions oldPermissions, long entityId,
            DiscordEntity entity) {
        super(channel);
        this.newPermissions = newPermissions;
        this.oldPermissions = oldPermissions;
        this.entityId = entityId;
        this.entity = entity;
    }

    @Override
    public Permissions getNewPermissions() {
        return newPermissions;
    }

    @Override
    public Permissions getOldPermissions() {
        return oldPermissions;
    }

    @Override
    public long getEntityId() {
        return entityId;
    }

    @Override
    public Optional<DiscordEntity> getEntity() {
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<User> getUser() {
        if (entity instanceof User) {
            return Optional.of((User) entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> getRole() {
        if (entity instanceof Role) {
            return Optional.of((Role) entity);
        }
        return Optional.empty();
    }

}
