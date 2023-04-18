package org.javacord.core.util.handler.guild.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUserAddEvent;
import org.javacord.core.event.server.scheduledevent.ServerScheduledEventUserAddEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild scheduled event user add packet.
 */
public class GuildScheduledEventUserAddHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildScheduledEventUserAddHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_USER_ADD");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerScheduledEvent scheduledEvent =
                    server.getScheduledEventById(packet.get("guild_scheduled_event_id").asLong())
                            .orElseThrow(AssertionError::new);

            ServerScheduledEventUserAddEvent event =
                    new ServerScheduledEventUserAddEventImpl(server, scheduledEvent, packet.get("user_id").asLong());

            api.getEventDispatcher()
                    .dispatchServerScheduledEventUserAddEvent((DispatchQueueSelector) server, server, event);
        });
    }

}
