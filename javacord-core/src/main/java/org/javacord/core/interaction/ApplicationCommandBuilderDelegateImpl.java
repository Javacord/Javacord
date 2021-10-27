package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.concurrent.CompletableFuture;

public abstract class ApplicationCommandBuilderDelegateImpl<T extends ApplicationCommand>
        implements ApplicationCommandBuilderDelegate<T> {

    protected String name;

    protected Boolean defaultPermission;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDefaultPermission(Boolean defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    @Override
    public CompletableFuture<T> createGlobal(DiscordApi api) {
        return new RestRequest<T>(api, RestMethod.POST, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()))
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> createInstance((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<T> createForServer(Server server) {
        return new RestRequest<T>(
                server.getApi(), RestMethod.POST, RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()), server.getIdAsString())
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> createInstance((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }

    /**
     * Gets the JSON body for this application command.
     *
     * @return The JSON body for this application command.
     */
    public ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = JsonNodeFactory.instance.objectNode()
                .put("name", name);

        if (defaultPermission != null) {
            jsonBody.put("default_permission", defaultPermission.booleanValue());
        }

        return jsonBody;
    }

    /**
     * Returns a created instance for the application command the builder is for.
     *
     * @param api      The DiscordApiImpl.
     * @param jsonNode The json of the application command.
     * @return An instance of application command from the JSON.
     */
    public abstract T createInstance(DiscordApiImpl api, JsonNode jsonNode);

}
