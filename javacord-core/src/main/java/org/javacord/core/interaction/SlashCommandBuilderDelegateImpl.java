package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.internal.SlashCommandBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SlashCommandBuilderDelegateImpl extends ApplicationCommandBuilderDelegateImpl<SlashCommand>
        implements SlashCommandBuilderDelegate {

    private String description;
    private List<SlashCommandOption> options = new ArrayList<>();

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addOption(SlashCommandOption option) {
        options.add(option);
    }

    @Override
    public void setOptions(List<SlashCommandOption> options) {
        if (options == null) {
            this.options.clear();
        } else {
            this.options = new ArrayList<>(options);
        }
    }

    @Override
    public CompletableFuture<SlashCommand> createGlobal(DiscordApi api) {
        return new RestRequest<SlashCommand>(api, RestMethod.POST, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()))
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> new SlashCommandImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<SlashCommand> createForServer(Server server) {
        return new RestRequest<SlashCommand>(
                server.getApi(), RestMethod.POST, RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()), server.getIdAsString())
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> new SlashCommandImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }

    /**
     * Gets the JSON body for this slash command.

     * @return The JSON of this slash command.
     */
    public ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = JsonNodeFactory.instance.objectNode()
                .put("name", name)
                .put("description", description);

        if (!options.isEmpty()) {
            ArrayNode jsonOptions = jsonBody.putArray("options");
            options.stream()
                    .map(SlashCommandOptionImpl.class::cast)
                    .map(SlashCommandOptionImpl::toJsonNode)
                    .forEach(jsonOptions::add);
        }

        if (defaultPermission != null) {
            jsonBody.put("default_permission", defaultPermission.booleanValue());
        }

        return jsonBody;
    }
}
