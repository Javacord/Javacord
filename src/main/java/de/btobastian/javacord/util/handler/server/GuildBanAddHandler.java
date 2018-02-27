package de.btobastian.javacord.util.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.event.server.member.ServerMemberBanEvent;
import de.btobastian.javacord.listener.server.member.ServerMemberBanListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the guild ban add packet.
 */
public class GuildBanAddHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildBanAddHandler(DiscordApi api) {
        super(api, true, "GUILD_BAN_ADD");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getServerById(packet.get("guild_id").asText())
                .map(server -> (ImplServer) server)
                .ifPresent(server -> {
                    User user = api.getOrCreateUser(packet.get("user"));
                    server.removeMember(user);

                    ServerMemberBanEvent event = new ServerMemberBanEvent(api, server, user);

                    List<ServerMemberBanListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberBanListeners());
                    listeners.addAll(user.getServerMemberBanListeners());
                    listeners.addAll(api.getServerMemberBanListeners());

                    api.getEventDispatcher()
                            .dispatchEvent(server, listeners, listener -> listener.onServerMemberBan(event));
                });
    }

}