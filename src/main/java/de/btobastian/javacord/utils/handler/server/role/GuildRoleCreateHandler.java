package de.btobastian.javacord.utils.handler.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.server.role.RoleCreateEvent;
import de.btobastian.javacord.listeners.server.role.RoleCreateListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

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
    public void handle(JSONObject packet) {
        long serverId = Long.parseLong(packet.getString("guild_id"));
        api.getServerById(serverId).ifPresent(server -> {
            Role role = ((ImplServer) server).getOrCreateRole(packet.getJSONObject("role"));
            RoleCreateEvent event = new RoleCreateEvent(role.getApi(), role);

            List<RoleCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getRoleCreateListeners());
            listeners.addAll(api.getRoleCreateListeners());

            dispatchEvent(listeners, listener -> listener.onRoleCreate(event));
        });
        // TODO update positions of old roles -> +1 for every role, besides @everyone
    }

}