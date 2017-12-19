package de.btobastian.javacord.entities.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.ApplicationInfo;
import de.btobastian.javacord.entities.User;

import java.util.Optional;

/**
 * The implementation of {@link ApplicationInfo}.
 */
public class ImplApplicationInfo implements ApplicationInfo {

    private final DiscordApi api;

    private final long clientId;
    private final String name;
    private final String description;
    private final boolean publicBot;
    private final boolean botRequiresCodeGrant;
    private final long ownerId;
    private final String ownerName;
    private final String ownerDiscriminator;

    /**
     * Creates a new application info object.
     *
     * @param api The discord api.
     * @param data The json data of the application.
     */
    public ImplApplicationInfo(DiscordApi api, JsonNode data) {
        this.api = api;

        clientId = data.get("id").asLong();
        name = data.get("name").asText();
        description = data.get("description").asText();
        publicBot = data.get("bot_public").asBoolean();
        botRequiresCodeGrant = data.get("bot_require_code_grant").asBoolean();
        ownerId = data.get("owner").get("id").asLong();
        ownerName = data.get("owner").get("username").asText();
        ownerDiscriminator = data.get("owner").get("discriminator").asText();
    }

    @Override
    public long getClientId() {
        return clientId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isPublicBot() {
        return publicBot;
    }

    @Override
    public boolean botRequiresCodeGrant() {
        return botRequiresCodeGrant;
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public long getOwnerId() {
        return ownerId;
    }

    @Override
    public String getOwnerDiscriminator() {
        return ownerDiscriminator;
    }

    @Override
    public Optional<User> getOwner() {
        return api.getUserById(getOwnerId());
    }
}
