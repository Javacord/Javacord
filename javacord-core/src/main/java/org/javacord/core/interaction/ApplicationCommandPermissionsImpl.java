package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ApplicationCommandPermissionType;
import org.javacord.api.interaction.ApplicationCommandPermissions;

import java.util.Optional;

public class ApplicationCommandPermissionsImpl implements ApplicationCommandPermissions {

    private final long id;
    private final Server server;
    private final ApplicationCommandPermissionType type;
    private final boolean permission;

    /**
     * Class constructor.
     *
     * @param server the server this application command permission is for.
     * @param data The json data of the application command permissions.
     */
    public ApplicationCommandPermissionsImpl(Server server, JsonNode data) {
        this.server = server;
        id = data.get("id").asLong();
        type = ApplicationCommandPermissionType.fromValue(data.get("type").asInt());
        permission = data.get("permission").asBoolean();
    }

    /**
     * Class constructor.
     *
     * @param server The server this permissions is for.
     * @param id The id.
     * @param type The type.
     * @param permission The permission.
     *
     */
    public ApplicationCommandPermissionsImpl(Server server, long id, ApplicationCommandPermissionType type,
                                             boolean permission) {
        this.server = server;
        this.id = id;
        this.type = type;
        this.permission = permission;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public ApplicationCommandPermissionType getType() {
        return type;
    }

    @Override
    public boolean getPermission() {
        return permission;
    }

    @Override
    public Optional<Role> getRole() {
        return type == ApplicationCommandPermissionType.ROLE ? server.getRoleById(id) : Optional.empty();
    }

    @Override
    public Optional<User> getUser() {
        return type == ApplicationCommandPermissionType.USER ? server.getApi().getCachedUserById(id) : Optional.empty();
    }

    @Override
    public Optional<ServerChannel> getChannel() {
        return type == ApplicationCommandPermissionType.CHANNEL
                ? id != (server.getId() - 1) ? server.getChannelById(id) : Optional.empty() : Optional.empty();
    }

    @Override
    public Server getServer() {
        return server;
    }

}
