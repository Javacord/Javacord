package org.javacord.core.util.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.entity.channel.GroupChannelImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

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
            new ServerImpl(api, guildJson);
        }

        // Private channels array is empty for bots, see
        // https://github.com/hammerandchisel/discord-api-docs/issues/184
        if (packet.has("private_channels")) {
            JsonNode privateChannels = packet.get("private_channels");
            for (JsonNode channelJson : privateChannels) {
                switch (channelJson.get("type").asInt()) {
                    case 1:
                        new PrivateChannelImpl(api, channelJson);
                        break;
                    case 3:
                        new GroupChannelImpl(api, channelJson);
                        break;
                    default:
                        LoggerUtil.getLogger(ReadyHandler.class).warn("Unknown channel type. Your Javacord version"
                                + " may be outdated.");

                }

            }
        }

        api.setYourself(api.getOrCreateUser(packet.get("user")));
    }

}
