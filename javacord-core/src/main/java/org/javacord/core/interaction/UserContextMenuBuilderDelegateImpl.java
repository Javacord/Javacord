package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.UserContextMenu;
import org.javacord.api.interaction.internal.UserContextMenuBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

public class UserContextMenuBuilderDelegateImpl extends ApplicationCommandBuilderDelegateImpl<UserContextMenu>
        implements UserContextMenuBuilderDelegate {

    @Override
    public CompletableFuture<UserContextMenu> createGlobal(DiscordApi api) {
        return new RestRequest<UserContextMenu>(api, RestMethod.POST, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()))
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> new UserContextMenuImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<UserContextMenu> createForServer(Server server) {
        return new RestRequest<UserContextMenu>(
                server.getApi(), RestMethod.POST, RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()), server.getIdAsString())
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> new UserContextMenuImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }

    /**
     * Gets the JSON body for this application command.

     * @return The JSON of this application command.
     */
    @Override
    public ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = JsonNodeFactory.instance.objectNode()
                .put("name", name);

        if (defaultPermission != null) {
            jsonBody.put("default_permission", defaultPermission.booleanValue());
        }

        jsonBody.put("type", ApplicationCommandType.USER.getValue());

        return jsonBody;
    }
}
