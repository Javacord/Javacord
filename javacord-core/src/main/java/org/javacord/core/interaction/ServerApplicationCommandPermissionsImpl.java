package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommandPermissions;
import org.javacord.api.interaction.ServerApplicationCommandPermissions;
import org.javacord.core.DiscordApiImpl;

import java.util.HashSet;
import java.util.Set;

public class ServerApplicationCommandPermissionsImpl implements ServerApplicationCommandPermissions {

    private final long id;
    private final long applicationId;
    private final Server server;
    private final Set<ApplicationCommandPermissions> permissions;

    /**
     * Class constructor.
     *
     * @param api The discord api instance.
     * @param data The json data of the application command permissions.
     */
    public ServerApplicationCommandPermissionsImpl(DiscordApiImpl api, JsonNode data) {
        id = data.get("id").asLong();
        applicationId = data.get("application_id").asLong();
        server = api.getPossiblyUnreadyServerById(data.get("guild_id").asLong()).orElseThrow(AssertionError::new);

        permissions = new HashSet<>();
        for (JsonNode jsonNode : data.get("permissions")) {
            permissions.add(new ApplicationCommandPermissionsImpl(server, jsonNode));
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getApplicationId() {
        return applicationId;
    }

    @Override
    public long getServerId() {
        return server.getId();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Set<ApplicationCommandPermissions> getPermissions() {
        return permissions;
    }
}
