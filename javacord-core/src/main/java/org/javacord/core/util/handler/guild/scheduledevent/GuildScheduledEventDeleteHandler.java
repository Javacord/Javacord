package org.javacord.core.util.handler.guild.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventDeleteEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.server.scheduledevent.ServerScheduledEventImpl;
import org.javacord.core.event.server.scheduledevent.ServerScheduledEventDeleteEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild scheduled event delete packet.
 */
public class GuildScheduledEventDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildScheduledEventDeleteHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_DELETE");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = Long.parseLong(packet.get("guild_id").asText());
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerImpl serverImpl = (ServerImpl) server;
            ServerScheduledEvent scheduledEvent = new ServerScheduledEventImpl(api, packet);
            serverImpl.removeScheduledEvent(scheduledEvent.getId());

            ServerScheduledEventDeleteEvent event = new ServerScheduledEventDeleteEventImpl(server, scheduledEvent);
            api.getEventDispatcher()
                    .dispatchServerScheduledEventDeleteEvent((DispatchQueueSelector) server, server, event);
        });
    }

}
