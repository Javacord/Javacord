package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerBecomesAvailableEvent;
import de.btobastian.javacord.events.server.ServerJoinEvent;
import de.btobastian.javacord.utils.PacketHandler;

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
    public void handle(JsonNode packet) {
        if (packet.has("unavailable") && packet.get("unavailable").asBoolean()) {
            return;
        }
        long id = packet.get("id").asLong();
        if (api.getUnavailableServers().contains(id)) {
            api.removeUnavailableServerFromCache(id);
            Server server = new ImplServer(api, packet);
            ServerBecomesAvailableEvent event = new ServerBecomesAvailableEvent(api, server);

            dispatchEvent(
                    api.getServerBecomesAvailableListeners(), listener -> listener.onServerBecomesAvailable(event));
            return;
        }

        Server server = new ImplServer(api, packet);
        ServerJoinEvent event = new ServerJoinEvent(api, server);

        dispatchEvent(api.getServerJoinListeners(), listener -> listener.onServerJoin(event));
    }

}