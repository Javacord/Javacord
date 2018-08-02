package org.javacord.core.util.handler.guild.role;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.role.RoleDeleteEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.role.RoleDeleteEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild role delete packet.
 */
public class GuildRoleDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildRoleDeleteHandler(DiscordApi api) {
        super(api, true, "GUILD_ROLE_DELETE");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = packet.get("guild_id").asLong();
        api.getPossiblyUnreadyServerById(serverId).map(server -> ((ServerImpl) server)).ifPresent(server -> {
            long roleId = packet.get("role_id").asLong();
            server.getRoleById(roleId).ifPresent(role -> {
                server.removeRole(roleId);

                RoleDeleteEvent event = new RoleDeleteEventImpl(role);

                api.getEventDispatcher().dispatchRoleDeleteEvent(server, role, server, event);
            });
        });
    }

}