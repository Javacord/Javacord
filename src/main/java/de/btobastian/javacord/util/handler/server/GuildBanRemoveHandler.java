package de.btobastian.javacord.util.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.event.server.member.ServerMemberUnbanEvent;
import de.btobastian.javacord.listener.server.member.ServerMemberUnbanListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

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

                    ServerMemberUnbanEvent event = new ServerMemberUnbanEvent(api, server, user);

                    List<ServerMemberUnbanListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberUnbanListeners());
                    listeners.addAll(user.getServerMemberUnbanListeners());
                    listeners.addAll(api.getServerMemberUnbanListeners());

                    api.getEventDispatcher()
                            .dispatchEvent(server, listeners, listener -> listener.onServerMemberUnban(event));
                });
    }

}