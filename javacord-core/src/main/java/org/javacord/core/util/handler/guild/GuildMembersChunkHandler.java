package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.member.Member;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.member.ServerMembersChunkEventImpl;
import org.javacord.core.util.gateway.PacketHandler;
import java.util.List;

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
                .map(ServerImpl.class::cast)
                .ifPresent(server -> {
                    List<Member> members = server.addAndGetMembers(packet.get("members"));
                    ServerMembersChunkEventImpl event = new ServerMembersChunkEventImpl(server, members);
                    api.getEventDispatcher().dispatchServerMembersChunkEvent(server, server, event);
                });
    }
}