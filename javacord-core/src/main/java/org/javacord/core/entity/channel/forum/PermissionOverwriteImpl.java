package org.javacord.core.entity.channel.forum;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.forum.PermissionOverwrite;

public class PermissionOverwriteImpl implements PermissionOverwrite {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The id of the overwrite.
     */
    private final long id;

    /**
     * The type of to overwrite.
     */
    private final int type;

    /**
     * The allowed permissions bit set.
     */
    private final int allowed;

    /**
     * The denied permissions bit set.
     */
    private final int denied;


    /**
     * Creates a new permission overwrite.
     * @param data The json data of the permission overwrite.
     */
    public PermissionOverwriteImpl(DiscordApi api, JsonNode data) {
        this.api = api;
        id = data.get("id").asLong();
        type = data.get("type").asInt();
        allowed = data.get("allow").asInt();
        denied = data.get("deny").asInt();
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getAllowed() {
        return allowed;
    }

    @Override
    public int getDenied() {
        return denied;
    }

    @Override
    public DiscordApi getApi() {
        return null;
    }

    @Override
    public long getId() {
        return id;
    }
}
