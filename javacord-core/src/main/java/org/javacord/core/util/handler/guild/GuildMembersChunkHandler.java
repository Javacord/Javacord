package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild members chunk packet.
 */
public class GuildMembersChunkHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMembersChunkHandler(DiscordApi api) {
        super(api, true, "GUILD_MEMBERS_CHUNK");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
            .map(server -> (ServerImpl) server)
            .ifPresent(server -> server.addMembers(packet.get("members")));
    }

}