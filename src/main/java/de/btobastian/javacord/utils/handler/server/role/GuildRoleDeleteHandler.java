package de.btobastian.javacord.utils.handler.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.role.RoleDeleteEvent;
import de.btobastian.javacord.listeners.server.role.RoleDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

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
    public void handle(JSONObject packet) {
        long serverId = Long.parseLong(packet.getString("guild_id"));
        api.getServerById(serverId).map(server -> ((ImplServer) server)).ifPresent(server -> {
            long roleId = Long.parseLong(packet.getString("role_id"));
            server.getRoleById(roleId).ifPresent(role -> {
                server.removeRole(roleId);

                RoleDeleteEvent event = new RoleDeleteEvent(role.getApi(), role);

                List<RoleDeleteListener> listeners = new ArrayList<>();
                listeners.addAll(role.getRoleDeleteListeners());
                listeners.addAll(server.getRoleDeleteListeners());
                listeners.addAll(api.getRoleDeleteListeners());

                dispatchEvent(listeners, listener -> listener.onRoleDelete(event));
            });
        });
    }

}