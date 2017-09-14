package de.btobastian.javacord.utils.handler.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerBecomesAvailableEvent;
import de.btobastian.javacord.events.server.ServerJoinEvent;
import de.btobastian.javacord.listeners.server.ServerBecomesAvailableListener;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the guild create packet.
 */
public class GuildCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildCreateHandler(DiscordApi api) {
        super(api, true, "GUILD_CREATE");
    }

    @Override
    public void handle(JSONObject packet) {
        if (packet.has("unavailable") && packet.getBoolean("unavailable")) {
            return;
        }
        long id = Long.valueOf(packet.getString("id"));
        if (api.getUnavailableServers().contains(id)) {
            api.removeUnavailableServerToCache(id);
            Server server = new ImplServer(api, packet);
            ServerBecomesAvailableEvent event = new ServerBecomesAvailableEvent(api, server);
            listenerExecutorService.submit(() -> {
                List<ServerBecomesAvailableListener> listeners = new ArrayList<>();
                listeners.addAll(api.getServerBecomesAvailableListeners());
                listeners.forEach(listener -> listener.onServerBecomesAvailable(event));
            });
            return;
        }

        Server server = new ImplServer(api, packet);
        ServerJoinEvent event = new ServerJoinEvent(api, server);
        listenerExecutorService.submit(() -> {
            List<ServerJoinListener> listeners = new ArrayList<>();
            listeners.addAll(api.getServerJoinListeners());
            listeners.forEach(listener -> listener.onServerJoin(event));
        });
    }

}