package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.util.gateway.PacketHandler;

public class GuildScheduledEventUserAddHandler extends PacketHandler {
    /**
     * Creates a new instance of this class.
     *
     * @param api   The api.
     */
    public GuildScheduledEventUserAddHandler(DiscordApi api) {
        super(api, true, "GUILD_SCHEDULED_EVENT_USER_ADD");
    }

    @Override
    protected void handle(JsonNode packet) {

    }
}
