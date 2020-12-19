package org.javacord.core.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.command.ApplicationCommand;
import org.javacord.api.command.ApplicationCommandOption;
import org.javacord.api.command.internal.ApplicationCommandBuilderDelegate;
import org.javacord.api.entity.server.Server;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationCommandBuilderDelegateImpl implements ApplicationCommandBuilderDelegate {

    private String name;
    private String description;
    private List<ApplicationCommandOption> options = new ArrayList<>();

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addOption(ApplicationCommandOption option) {
        options.add(option);
    }

    @Override
    public void setOptions(List<ApplicationCommandOption> options) {
        if (options == null) {
            this.options.clear();
        } else {
            this.options = new ArrayList<>(options);
        }
    }

    @Override
    public CompletableFuture<ApplicationCommand> createGlobal(DiscordApi api) {
        return new RestRequest<ApplicationCommand>(api, RestMethod.POST, RestEndpoint.APPLICATION_COMMANDS)
            .setUrlParameters(String.valueOf(api.getClientId()))
            .setBody(getJsonBodyForApplicationCommand())
            .execute(result -> new ApplicationCommandImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<ApplicationCommand> createForServer(Server server) {
        return new RestRequest<ApplicationCommand>(
            server.getApi(), RestMethod.POST, RestEndpoint.GUILD_APPLICATION_COMMANDS)
            .setUrlParameters(String.valueOf(server.getApi().getClientId()), server.getIdAsString())
            .setBody(getJsonBodyForApplicationCommand())
            .execute(result -> new ApplicationCommandImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }

    private ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = JsonNodeFactory.instance.objectNode()
                .put("name", name)
                .put("description", description);

        if (!options.isEmpty()) {
            ArrayNode jsonOptions = jsonBody.putArray("options");
            options.stream()
                .map(ApplicationCommandOptionImpl.class::cast)
                .map(ApplicationCommandOptionImpl::toJsonNode)
                .forEach(jsonOptions::add);
        }

        return jsonBody;
    }
}
