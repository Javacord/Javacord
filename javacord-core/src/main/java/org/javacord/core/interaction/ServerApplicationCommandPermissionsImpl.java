package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.interaction.ApplicationCommandPermissions;
import org.javacord.api.interaction.ServerApplicationCommandPermissions;

import java.util.ArrayList;
import java.util.List;

public class ServerApplicationCommandPermissionsImpl implements ServerApplicationCommandPermissions {

    private final long id;
    private final long applicationId;
    private final long serverId;
    private final List<ApplicationCommandPermissions> permissions;

    /**
     * Class constructor.
     *
     * @param data The json data of the application command permissions.
     */
    public ServerApplicationCommandPermissionsImpl(JsonNode data) {
        id = data.get("id").asLong();
        applicationId = data.get("application_id").asLong();
        serverId = data.get("guild_id").asLong();

        permissions = new ArrayList<>();
        for (JsonNode jsonNode : data.get("permissions")) {
            permissions.add(new ApplicationCommandPermissionsImpl(jsonNode));
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
        return serverId;
    }

    @Override
    public List<ApplicationCommandPermissions> getPermissions() {
        return permissions;
    }
}
