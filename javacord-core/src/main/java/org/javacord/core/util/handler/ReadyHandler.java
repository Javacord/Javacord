package org.javacord.core.util.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

/**
 * This class handles the ready packet.
 */
public class ReadyHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ReadyHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ReadyHandler(DiscordApi api) {
        super(api, false, "READY");
    }

    @Override
    public void handle(JsonNode packet) {
        // Purge the cache first
        api.purgeCache();

        JsonNode guilds = packet.get("guilds");
        for (JsonNode guildJson : guilds) {
            if (guildJson.has("unavailable") && guildJson.get("unavailable").asBoolean()) {
                api.addUnavailableServerToCache(guildJson.get("id").asLong());
                continue;
            }
            new ServerImpl(api, guildJson);
        }

        api.setYourself(new UserImpl(api, packet.get("user")));
    }

}
