package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerBecomesUnavailableEvent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.core.event.server.ServerBecomesUnavailableEventImpl;
import org.javacord.core.event.server.ServerLeaveEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

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

                api.getEventDispatcher().dispatchServerBecomesUnavailableEvent(
                        (DispatchQueueSelector) server, server, event);
                api.forEachCachedMessageWhere(
                        msg -> msg.getServer().map(s -> s.getId() == serverId).orElse(false),
                        msg -> api.removeMessageFromCache(msg.getId())
                );
            });
            api.removeServerFromCache(serverId);
            return;
        }
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerLeaveEvent event = new ServerLeaveEventImpl(server);

            api.getEventDispatcher().dispatchServerLeaveEvent((DispatchQueueSelector) server, server, event);
        });
        api.removeObjectListeners(Server.class, serverId);
        api.forEachCachedMessageWhere(
                msg -> msg.getServer().map(s -> s.getId() == serverId).orElse(false),
                msg -> api.removeMessageFromCache(msg.getId())
        );
        api.removeServerFromCache(serverId);
    }

}
