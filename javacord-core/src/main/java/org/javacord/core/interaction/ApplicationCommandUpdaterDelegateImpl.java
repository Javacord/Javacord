package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.ApplicationCommandOption;
import org.javacord.api.interaction.internal.ApplicationCommandUpdaterDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ApplicationCommandUpdaterDelegate}.
 */
public class ApplicationCommandUpdaterDelegateImpl implements ApplicationCommandUpdaterDelegate {

    /**
     * The application command id.
     */
    private final long commandId;

    /**
     * The application command name.
     */
    private String name = null;

    /**
     * The application command description.
     */
    private String description = null;

    /**
     * The application command options.
     */
    private List<ApplicationCommandOption> applicationCommandOptions = new ArrayList<>();

    /**
     * The application command default permission.
     */
    private boolean defaultPermission = true;

    /**
     * Creates a new account updater delegate.
     *
     * @param commandId The discord api instance.
     */
    public ApplicationCommandUpdaterDelegateImpl(long commandId) {
        this.commandId = commandId;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setOptions(List<ApplicationCommandOption> applicationCommandOptions) {
        this.applicationCommandOptions = applicationCommandOptions;
    }

    @Override
    public void setDefaultPermission(boolean defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    private void prepareBody(ObjectNode body) {
        if (name != null && !name.isEmpty()) {
            body.put("name", name);
        }

        if (description != null && !description.isEmpty()) {
            body.put("description", description);
        }

        if (!applicationCommandOptions.isEmpty()) {
            ArrayNode array = body.putArray("options");
            for (ApplicationCommandOption applicationCommandOption : applicationCommandOptions) {
                array.add(((ApplicationCommandOptionImpl) applicationCommandOption).toJsonNode());
            }
        }

        body.put("default_permission", defaultPermission);
    }

    @Override
    public CompletableFuture<ApplicationCommand> updateGlobal(DiscordApi api) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<ApplicationCommand>(api, RestMethod.PATCH, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new ApplicationCommandImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<ApplicationCommand> updateForServer(Server server) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<ApplicationCommand>(server.getApi(), RestMethod.PATCH,
                RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()),
                        server.getIdAsString(), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new ApplicationCommandImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }
}
