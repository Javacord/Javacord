package org.javacord.core.util.handler.guild.role;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleCreateEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.role.RoleCreateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild role create packet.
 */
public class GuildRoleCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildRoleCreateHandler(DiscordApi api) {
        super(api, true, "GUILD_ROLE_CREATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            Role role = ((ServerImpl) server).getOrCreateRole(packet.get("role"));
            RoleCreateEvent event = new RoleCreateEventImpl(role);

            api.getEventDispatcher().dispatchRoleCreateEvent((DispatchQueueSelector) server, server, event);
        });
    }

}
