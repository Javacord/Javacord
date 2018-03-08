package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.event.server.ServerBecomesUnavailableEvent;
import org.javacord.event.server.ServerLeaveEvent;
import org.javacord.event.server.impl.ImplServerBecomesUnavailableEvent;
import org.javacord.event.server.impl.ImplServerLeaveEvent;
import org.javacord.listener.server.ServerBecomesUnavailableListener;
import org.javacord.listener.server.ServerLeaveListener;
import org.javacord.util.gateway.PacketHandler;

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
                ServerBecomesUnavailableEvent event = new ImplServerBecomesUnavailableEvent(server);

                List<ServerBecomesUnavailableListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerBecomesUnavailableListeners());
                listeners.addAll(api.getServerBecomesUnavailableListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerBecomesUnavailable(event));
            });
            api.removeServerFromCache(serverId);
            return;
        }
        api.getServerById(serverId).ifPresent(server -> {
            ServerLeaveEvent event = new ImplServerLeaveEvent(server);

            List<ServerLeaveListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerLeaveListeners());
            listeners.addAll(api.getServerLeaveListeners());

            api.getEventDispatcher().dispatchEvent(server, listeners, listener -> listener.onServerLeave(event));
        });
        api.removeServerFromCache(serverId);
    }

}