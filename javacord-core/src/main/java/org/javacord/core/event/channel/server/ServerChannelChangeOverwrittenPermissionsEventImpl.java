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
    public ServerChannelChangeOverwrittenPermissionsEventImpl(
            ServerChannel channel, Permissions newPermissions, Permissions oldPermissions, DiscordEntity entity) {
        super(channel);
        this.newPermissions = newPermissions;
        this.oldPermissions = oldPermissions;
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
    public DiscordEntity getEntity() {
        return entity;
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
