package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.server.member.ServerMemberLeaveEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild member remove packet.
 */
public class GuildMemberRemoveHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMemberRemoveHandler(DiscordApi api) {
        super(api, true, "GUILD_MEMBER_REMOVE");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    User user = new UserImpl(api, packet.get("user"));
                    server.removeMember(user.getId());
                    server.decrementMemberCount();

                    ServerMemberLeaveEvent event = new ServerMemberLeaveEventImpl(server, user);

                    api.getEventDispatcher().dispatchServerMemberLeaveEvent(server, server, user, event);
                    
                });
    }

}
