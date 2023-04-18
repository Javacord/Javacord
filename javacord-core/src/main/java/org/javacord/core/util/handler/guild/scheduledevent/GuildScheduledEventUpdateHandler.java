package org.javacord.core.util.handler.guild.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUpdateEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.server.scheduledevent.ServerScheduledEventImpl;
import org.javacord.core.event.server.scheduledevent.ServerScheduledEventUpdateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild scheduled event update create packet.
 */
public class GuildScheduledEventUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildScheduledEventUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerImpl serverImpl = (ServerImpl) server;
            ServerScheduledEvent scheduledEvent = new ServerScheduledEventImpl(api, packet);
            ServerScheduledEvent oldScheduledEvent =
                    serverImpl.getScheduledEventById(scheduledEvent.getId()).orElseThrow(AssertionError::new);

            serverImpl.removeScheduledEvent(scheduledEvent.getId());
            serverImpl.addScheduledEvent(scheduledEvent);

            ServerScheduledEventUpdateEvent event =
                    new ServerScheduledEventUpdateEventImpl(server, oldScheduledEvent, scheduledEvent);
            api.getEventDispatcher()
                    .dispatchServerScheduledEventUpdateEvent((DispatchQueueSelector) server, server, event);
        });
    }

}
