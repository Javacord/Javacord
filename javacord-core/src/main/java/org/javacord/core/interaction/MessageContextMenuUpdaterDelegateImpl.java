package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.MessageContextMenu;
import org.javacord.api.interaction.internal.MessageContextMenuUpdaterDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

public class MessageContextMenuUpdaterDelegateImpl extends ApplicationCommandUpdaterDelegateImpl<MessageContextMenu>
        implements MessageContextMenuUpdaterDelegate {

    /**
     * Creates a new message context menu updater delegate.
     *
     * @param commandId The discord api instance.
     */
    public MessageContextMenuUpdaterDelegateImpl(long commandId) {
        this.commandId = commandId;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CompletableFuture<MessageContextMenu> updateGlobal(DiscordApi api) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<MessageContextMenu>(api, RestMethod.PATCH, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new MessageContextMenuImpl((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<MessageContextMenu> updateForServer(DiscordApi api, long server) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        return new RestRequest<MessageContextMenu>(api, RestMethod.PATCH,
                RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()),
                        String.valueOf(server), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new MessageContextMenuImpl((DiscordApiImpl) api, result.getJsonBody()));
    }
}
