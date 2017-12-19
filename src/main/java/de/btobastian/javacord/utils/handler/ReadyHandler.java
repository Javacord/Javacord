package de.btobastian.javacord.utils.handler;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.impl.ImplGroupChannel;
import de.btobastian.javacord.entities.channels.impl.ImplPrivateChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.utils.PacketHandler;

/**
 * This class handles the ready packet.
 */
public class ReadyHandler extends PacketHandler {

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
            new ImplServer(api, guildJson);
        }

        // Private channels array is empty for bots, see
        // https://github.com/hammerandchisel/discord-api-docs/issues/184
        if (packet.has("private_channels")) {
            JsonNode privateChannels = packet.get("private_channels");
            for (JsonNode channelJson : privateChannels) {
                switch (channelJson.get("type").asInt()) {
                    case 1:
                        new ImplPrivateChannel(api, channelJson);
                        break;
                    case 3:
                        new ImplGroupChannel(api, channelJson);
                        break;
                }

            }
        }

        api.setYourself(api.getOrCreateUser(packet.get("user")));
    }

}