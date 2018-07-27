package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.ServerBecomesUnavailableEvent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerBecomesUnavailableListener;
import org.javacord.api.listener.server.ServerLeaveListener;
import org.javacord.core.event.server.ServerBecomesUnavailableEventImpl;
import org.javacord.core.event.server.ServerLeaveEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
            api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
                ServerBecomesUnavailableEvent event = new ServerBecomesUnavailableEventImpl(server);

                List<ServerBecomesUnavailableListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerBecomesUnavailableListeners());
                listeners.addAll(api.getServerBecomesUnavailableListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerBecomesUnavailable(event));
            });
            api.removeServerFromCache(serverId);
            return;
        }
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerLeaveEvent event = new ServerLeaveEventImpl(server);

            List<ServerLeaveListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerLeaveListeners());
            listeners.addAll(api.getServerLeaveListeners());

            api.getEventDispatcher().dispatchEvent(server, listeners, listener -> listener.onServerLeave(event));
        });
        api.removeServerFromCache(serverId);
    }

}