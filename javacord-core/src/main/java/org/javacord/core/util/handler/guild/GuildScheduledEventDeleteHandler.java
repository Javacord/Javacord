package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
        });
    }
}
