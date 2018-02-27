package de.btobastian.javacord.util.handler.server.role;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.event.server.role.RoleCreateEvent;
import de.btobastian.javacord.listener.server.role.RoleCreateListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

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
        api.getServerById(serverId).ifPresent(server -> {
            Role role = ((ImplServer) server).getOrCreateRole(packet.get("role"));
            RoleCreateEvent event = new RoleCreateEvent(role.getApi(), role);

            List<RoleCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getRoleCreateListeners());
            listeners.addAll(api.getRoleCreateListeners());

            api.getEventDispatcher().dispatchEvent(server, listeners, listener -> listener.onRoleCreate(event));
        });
    }

}