package org.javacord.core.util.handler.guild.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventCreateEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.server.scheduledevent.ServerScheduledEventImpl;
import org.javacord.core.event.server.scheduledevent.ServerScheduledEventCreateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild scheduled event create packet.
 */
public class GuildScheduledEventCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildScheduledEventCreateHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_CREATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerImpl serverImpl = (ServerImpl) server;
            ServerScheduledEvent scheduledEvent = new ServerScheduledEventImpl(api, packet);
            serverImpl.addScheduledEvent(scheduledEvent);

            ServerScheduledEventCreateEvent event = new ServerScheduledEventCreateEventImpl(server, scheduledEvent);
            api.getEventDispatcher()
                    .dispatchServerScheduledEventCreateEvent((DispatchQueueSelector) server, server, event);
        });
    }

}
