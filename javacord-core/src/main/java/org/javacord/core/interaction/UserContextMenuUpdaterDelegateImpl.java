package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.UserContextMenu;
import org.javacord.api.interaction.internal.UserContextMenuUpdaterDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

public class UserContextMenuUpdaterDelegateImpl extends ApplicationCommandUpdaterDelegateImpl<UserContextMenu>
        implements UserContextMenuUpdaterDelegate {

    /**
     * Creates a new user context menu updater delegate.
     *
     * @param commandId The discord api instance.
     */
    public UserContextMenuUpdaterDelegateImpl(long commandId) {
        this.commandId = commandId;
    }

    private void prepareBody(ObjectNode body) {
        if (name != null && !name.isEmpty()) {
            body.put("name", name);
        }

        body.put("default_permission", defaultPermission);
    }

    @Override
    public CompletableFuture<UserContextMenu> updateGlobal(DiscordApi api) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<UserContextMenu>(api, RestMethod.PATCH, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new UserContextMenuImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<UserContextMenu> updateForServer(Server server) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<UserContextMenu>(server.getApi(), RestMethod.PATCH,
                RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()),
                        server.getIdAsString(), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new UserContextMenuImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }
}
