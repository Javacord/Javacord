package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.ApplicationCommandPermissionsUpdateEvent;
import org.javacord.api.interaction.ApplicationCommandPermissions;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.ApplicationCommandPermissionsUpdateEventImpl;
import org.javacord.core.interaction.ApplicationCommandPermissionsImpl;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles the Application Command Permissions Update packet.
 */
public class ApplicationCommandPermissionsUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ApplicationCommandPermissionsUpdateHandler(DiscordApi api) {
        super(api, true, "APPLICATION_COMMAND_PERMISSIONS_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    long commandId = packet.get("id").asLong();
                    long applicationId = packet.get("application_id").asLong();
                    Set<ApplicationCommandPermissions> newPermissions = new HashSet<>();
                    packet.get("permissions").elements().forEachRemaining(permission ->
                            newPermissions.add(new ApplicationCommandPermissionsImpl(server, permission)));

                    ApplicationCommandPermissionsUpdateEvent event =
                            new ApplicationCommandPermissionsUpdateEventImpl(applicationId, commandId,
                                    server, newPermissions);
                    api.getEventDispatcher().dispatchApplicationCommandPermissionsUpdateEvent(server, server, event);
                });
    }
}
