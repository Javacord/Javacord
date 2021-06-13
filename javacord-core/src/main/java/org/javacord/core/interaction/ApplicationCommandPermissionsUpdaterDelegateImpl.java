package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommandPermissions;
import org.javacord.api.interaction.ServerApplicationCommandPermissions;
import org.javacord.api.interaction.internal.ApplicationCommandPermissionsUpdaterDelegate;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationCommandPermissionsUpdaterDelegateImpl implements ApplicationCommandPermissionsUpdaterDelegate {

    private final Server server;
    private List<ApplicationCommandPermissions> permissions;

    /**
     * Creates an instance of this class.
     *
     * @param server The server where the update should be performed on.
     */
    public ApplicationCommandPermissionsUpdaterDelegateImpl(Server server) {
        this.server = server;
        this.permissions = new ArrayList<>();
    }

    @Override
    public void setPermissions(List<ApplicationCommandPermissions> permission) {
        this.permissions = permission;
    }

    @Override
    public void addPermissions(List<ApplicationCommandPermissions> permissions) {
        this.permissions.addAll(permissions);
    }

    @Override
    public void addPermission(ApplicationCommandPermissions permission) {
        this.permissions.add(permission);
    }

    @Override
    public CompletableFuture<ServerApplicationCommandPermissions> update(long commandId) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        ArrayNode array = body.putArray("permissions");
        for (ApplicationCommandPermissions permission : permissions) {
            array.add(((ApplicationCommandPermissionsImpl) permission).toJsonNode());
        }

        return new RestRequest<ServerApplicationCommandPermissions>(server.getApi(), RestMethod.PUT,
                RestEndpoint.APPLICATION_COMMAND_PERMISSIONS)
                .setUrlParameters(String.valueOf(server.getApi().getClientId()),
                        server.getIdAsString(), String.valueOf(commandId))
                .setBody(body)
                .execute(result -> new ServerApplicationCommandPermissionsImpl(result.getJsonBody()));
    }
}
