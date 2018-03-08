package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberUnbanEvent;
import org.javacord.event.server.member.impl.ImplServerMemberUnbanEvent;
import org.javacord.listener.server.member.ServerMemberUnbanListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

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
        api.getServerById(packet.get("guild_id").asText())
                .map(server -> (ImplServer) server)
                .ifPresent(server -> {
                    User user = api.getOrCreateUser(packet.get("user"));

                    ServerMemberUnbanEvent event = new ImplServerMemberUnbanEvent(server, user);

                    List<ServerMemberUnbanListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberUnbanListeners());
                    listeners.addAll(user.getServerMemberUnbanListeners());
                    listeners.addAll(api.getServerMemberUnbanListeners());

                    api.getEventDispatcher()
                            .dispatchEvent(server, listeners, listener -> listener.onServerMemberUnban(event));
                });
    }

}