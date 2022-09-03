package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.util.gateway.PacketHandler;

public class GuildScheduledEventUpdateHandler extends PacketHandler {
    /**
     * Creates a new instance of this class.
     *
     * @param api   The api.
     */
    public GuildScheduledEventUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_UPDATE");
    }

    @Override
    protected void handle(JsonNode packet) {

    }
}
