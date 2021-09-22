package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.internal.SlashCommandUpdaterDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link SlashCommandUpdaterDelegate}.
 */
public class SlashCommandUpdaterDelegateImpl extends ApplicationCommandUpdaterDelegateImpl<SlashCommand>
        implements SlashCommandUpdaterDelegate {

    /**
     * The slash command description.
     */
    private String description = null;

    /**
     * The slash command options.
     */
    private List<SlashCommandOption> slashCommandOptions = new ArrayList<>();

    /**
     * Creates a new account updater delegate.
     *
     * @param commandId The discord api instance.
     */
    public SlashCommandUpdaterDelegateImpl(long commandId) {
        this.commandId = commandId;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setOptions(List<SlashCommandOption> slashCommandOptions) {
        this.slashCommandOptions = slashCommandOptions;
    }

    private void prepareBody(ObjectNode body) {
        if (name != null && !name.isEmpty()) {
            body.put("name", name);
        }

        if (description != null && !description.isEmpty()) {
            body.put("description", description);
        }

        if (!slashCommandOptions.isEmpty()) {
            ArrayNode array = body.putArray("options");
            for (SlashCommandOption slashCommandOption : slashCommandOptions) {
                array.add(((SlashCommandOptionImpl) slashCommandOption).toJsonNode());
            }
        }

        body.put("default_permission", defaultPermission);
    }

    @Override
    public CompletableFuture<SlashCommand> updateGlobal(DiscordApi api) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<SlashCommand>(api, RestMethod.PATCH, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new SlashCommandImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<SlashCommand> updateForServer(Server server) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<SlashCommand>(server.getApi(), RestMethod.PATCH,
                RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()),
                        server.getIdAsString(), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new SlashCommandImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }
}
