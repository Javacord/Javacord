package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.interaction.ServerSlashCommandPermissions;
import org.javacord.api.interaction.SlashCommandPermissions;

import java.util.ArrayList;
import java.util.List;

public class ServerSlashCommandPermissionsImpl implements ServerSlashCommandPermissions {

    private final long id;
    private final long applicationId;
    private final long serverId;
    private final List<SlashCommandPermissions> permissions;

    /**
     * Class constructor.
     *
     * @param data The json data of the slash command permissions.
     */
    public ServerSlashCommandPermissionsImpl(JsonNode data) {
        id = data.get("id").asLong();
        applicationId = data.get("application_id").asLong();
        serverId = data.get("guild_id").asLong();

        permissions = new ArrayList<>();
        for (JsonNode jsonNode : data.get("permissions")) {
            permissions.add(new SlashCommandPermissionsImpl(jsonNode));
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
    public List<SlashCommandPermissions> getPermissions() {
        return permissions;
    }
}
