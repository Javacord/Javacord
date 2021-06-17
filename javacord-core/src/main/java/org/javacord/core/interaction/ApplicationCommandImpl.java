package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.ApplicationCommandOption;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationCommandImpl implements ApplicationCommand {

    private final DiscordApiImpl api;
    private final long id;
    private final long applicationId;
    private final String name;
    private final String description;
    private final List<ApplicationCommandOption> options;
    private final boolean defaultPermission;

    /**
     * Class constructor.
     *
     * @param api The api instance.
     * @param data The json data of the application command.
     */
    public ApplicationCommandImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        id = data.get("id").asLong();
        applicationId = data.get("application_id").asLong();
        name = data.get("name").asText();
        description = data.get("description").asText();
        options = new ArrayList<>();
        if (data.has("options")) {
            for (JsonNode optionJson : data.get("options")) {
                options.add(new ApplicationCommandOptionImpl(optionJson));
            }
        }
        defaultPermission = !data.hasNonNull("default_permission") || data.get("default_permission").asBoolean();
    }

    @Override
    public DiscordApi getApi() {
        return api;
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
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<ApplicationCommandOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public boolean getDefaultPermission() {
        return defaultPermission;
    }

    @Override
    public CompletableFuture<Void> deleteGlobal() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.APPLICATION_COMMANDS)
            .setUrlParameters(String.valueOf(getApplicationId()), getIdAsString())
            .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> deleteForServer(Server server) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_APPLICATION_COMMANDS)
            .setUrlParameters(String.valueOf(getApplicationId()), server.getIdAsString(), getIdAsString())
            .execute(result -> null);
    }
}
