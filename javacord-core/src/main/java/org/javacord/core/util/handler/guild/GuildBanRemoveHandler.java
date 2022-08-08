package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberUnbanEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.server.member.ServerMemberUnbanEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild ban add packet.
 */
public class GuildBanRemoveHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildBanRemoveHandler(DiscordApi api) {
        super(api, true, "GUILD_BAN_REMOVE");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    User user = new UserImpl(api, packet.get("user"));

                    ServerMemberUnbanEvent event = new ServerMemberUnbanEventImpl(server, user);

                    api.getEventDispatcher().dispatchServerMemberUnbanEvent(server, server, user, event);
                });
    }

}