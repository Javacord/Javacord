package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.events.server.ServerBecomesUnavailableEvent;
import de.btobastian.javacord.events.server.ServerLeaveEvent;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the guild delete packet.
 */
public class GuildDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildDeleteHandler(DiscordApi api) {
        super(api, true, "GUILD_DELETE");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = packet.get("id").asLong();
        if (packet.has("unavailable") && packet.get("unavailable").asBoolean()) {
            api.addUnavailableServerToCache(serverId);
            api.getServerById(serverId).ifPresent(server -> {
                ServerBecomesUnavailableEvent event = new ServerBecomesUnavailableEvent(api, server);

                List<ServerBecomesUnavailableListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerBecomesUnavailableListeners());
                listeners.addAll(api.getServerBecomesUnavailableListeners());

                dispatchEvent(listeners, listener -> listener.onServerBecomesUnavailable(event));
            });
            api.removeServerFromCache(serverId);
            return;
        }
        api.getServerById(serverId).ifPresent(server -> {
            ServerLeaveEvent event = new ServerLeaveEvent(api, server);

            List<ServerLeaveListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerLeaveListeners());
            listeners.addAll(api.getServerLeaveListeners());

            dispatchEvent(listeners, listener -> listener.onServerLeave(event));
        });
        api.removeServerFromCache(serverId);
    }

}