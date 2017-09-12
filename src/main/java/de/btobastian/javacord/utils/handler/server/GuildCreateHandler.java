package de.btobastian.javacord.utils.handler.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerJoinEvent;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.utils.PacketHandler;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the guild create packet.
 */
public class GuildCreateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GuildCreateHandler.class);

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
            api.getUnavailableServers().remove(id);
            new ImplServer(api, packet);
            return;
        }

        final Server server = new ImplServer(api, packet);
        ServerJoinEvent event = new ServerJoinEvent(api, server);
        listenerExecutorService.submit(() -> {
            List<ServerJoinListener> listeners = new ArrayList<>();
            listeners.addAll(api.getServerJoinListeners());
            listeners.forEach(listener -> listener.onServerJoin(event));
        });
    }

}