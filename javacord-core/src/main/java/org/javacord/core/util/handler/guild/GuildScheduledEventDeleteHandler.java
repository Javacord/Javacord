package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.event.server.schedule.ServerScheduledDeleteEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.schedule.ServerScheduledDeleteEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.concurrent.ExecutionException;

public class GuildScheduledEventDeleteHandler extends PacketHandler {
    /**
     * Creates a new instance of this class.
     *
     * @param api   The api.
     */
    public GuildScheduledEventDeleteHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_DELETE");
    }

    @Override
    protected void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).map(server -> ((ServerImpl) server)).ifPresent(server -> {
           long scheduledEventId = Long.parseLong(packet.get("id").asText());
            try {
                ScheduledEvent scheduledEvent = server.getScheduledEventById(scheduledEventId).get();
                ServerScheduledDeleteEvent event = new ServerScheduledDeleteEventImpl(server, scheduledEvent);
                api.getEventDispatcher().dispatchServerScheduledDeleteEvent(server, server, event);;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
