package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.member.ServerMemberJoinEvent;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;

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
        api.getServerById(packet.get("guild_id").asText())
                .map(server -> (ImplServer) server)
                .ifPresent(server -> {
                    server.addMember(packet);
                    User user = api.getOrCreateUser(packet.get("user"));

                    ServerMemberJoinEvent event = new ServerMemberJoinEvent(api, server, user);

                    List<ServerMemberJoinListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberJoinListeners());
                    listeners.addAll(user.getServerMemberJoinListeners());
                    listeners.addAll(api.getServerMemberJoinListeners());

                    dispatchEvent(listeners, listener -> listener.onServerMemberJoin(event));
                });
    }

}