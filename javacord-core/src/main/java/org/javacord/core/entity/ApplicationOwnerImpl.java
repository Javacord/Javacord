package org.javacord.core.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.ApplicationOwner;
import org.javacord.api.entity.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * An implementation for {@link ApplicationOwner}.
 */
public class ApplicationOwnerImpl implements ApplicationOwner {

    private final DiscordApi api;
    private final long id;
    private final String name;
    private final String discriminator;

    /**
     * Construct the owner from json.
     * @param api The discord api instance.
     * @param data The json node.
     */
    public ApplicationOwnerImpl(DiscordApi api, JsonNode data) {
        this.api = api;
        id = data.get("id").asLong();
        name = data.get("username").asText();
        discriminator = data.get("discriminator").asText();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public CompletableFuture<User> requestUser() {
        return api.getUserById(id);
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }
}
