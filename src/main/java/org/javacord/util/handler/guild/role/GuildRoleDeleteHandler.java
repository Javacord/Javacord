package org.javacord.util.handler.guild.role;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.event.server.role.RoleDeleteEvent;
import org.javacord.event.server.role.impl.ImplRoleDeleteEvent;
import org.javacord.listener.server.role.RoleDeleteListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

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
        api.getServerById(serverId).map(server -> ((ImplServer) server)).ifPresent(server -> {
            long roleId = packet.get("role_id").asLong();
            server.getRoleById(roleId).ifPresent(role -> {
                server.removeRole(roleId);

                RoleDeleteEvent event = new ImplRoleDeleteEvent(role);

                List<RoleDeleteListener> listeners = new ArrayList<>();
                listeners.addAll(role.getRoleDeleteListeners());
                listeners.addAll(server.getRoleDeleteListeners());
                listeners.addAll(api.getRoleDeleteListeners());

                api.getEventDispatcher().dispatchEvent(server, listeners, listener -> listener.onRoleDelete(event));
            });
        });
    }

}