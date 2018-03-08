package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.event.server.ServerBecomesAvailableEvent;
import org.javacord.event.server.ServerJoinEvent;
import org.javacord.event.server.impl.ImplServerBecomesAvailableEvent;
import org.javacord.event.server.impl.ImplServerJoinEvent;
import org.javacord.util.gateway.PacketHandler;

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
            ServerBecomesAvailableEvent event = new ImplServerBecomesAvailableEvent(server);

            api.getEventDispatcher().dispatchEvent(server,
                    api.getServerBecomesAvailableListeners(), listener -> listener.onServerBecomesAvailable(event));
            return;
        }

        Server server = new ImplServer(api, packet);
        ServerJoinEvent event = new ImplServerJoinEvent(server);

        api.getEventDispatcher().dispatchEvent(server, api.getServerJoinListeners(), listener -> listener.onServerJoin(event));
    }

}