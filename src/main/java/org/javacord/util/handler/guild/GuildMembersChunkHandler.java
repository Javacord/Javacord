package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.util.gateway.PacketHandler;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.util.gateway.PacketHandler;

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
        api.getServerById(packet.get("guild_id").asText())
            .map(server -> (ImplServer) server)
            .ifPresent(server -> server.addMembers(packet.get("members")));
    }

}