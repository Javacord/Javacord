package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.core.entity.member.MemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.member.ServerMemberJoinEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild member add packet.
 */
public class GuildMemberAddHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMemberAddHandler(DiscordApi api) {
        super(api, true, "GUILD_MEMBER_ADD");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    MemberImpl member = server.addMember(packet);
                    server.incrementMemberCount();

                    ServerMemberJoinEvent event = new ServerMemberJoinEventImpl(member);

                    api.getEventDispatcher().dispatchServerMemberJoinEvent(server, server, member.getUser(), event);
                });
    }

}