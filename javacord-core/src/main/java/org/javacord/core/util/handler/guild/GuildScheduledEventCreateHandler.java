package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.schedule.ServerScheduledCreateEvent;
import org.javacord.core.entity.server.ScheduledEventImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.schedule.ServerScheduledCreateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

public class GuildScheduledEventCreateHandler extends PacketHandler {
    /**
     * Creates a new instance of this class.
     *
     * @param api   The api.
     */
    public GuildScheduledEventCreateHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_CREATE");
    }

    @Override
    protected void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ScheduledEventImpl scheduledEvent = (ScheduledEventImpl) ((ServerImpl) server)
                    .getOrCreateScheduledEvent(packet.get("scheduled_event"));
            ServerScheduledCreateEvent event = new ServerScheduledCreateEventImpl(server, scheduledEvent);
            api.getEventDispatcher().dispatchServerScheduledCreateEvent((DispatchQueueSelector) server, server, event);
        });
    }
}
