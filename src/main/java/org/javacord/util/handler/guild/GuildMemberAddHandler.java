package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberJoinEvent;
import org.javacord.event.server.member.impl.ImplServerMemberJoinEvent;
import org.javacord.listener.server.member.ServerMemberJoinListener;
import org.javacord.util.gateway.PacketHandler;

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
                    server.incrementMemberCount();
                    User user = api.getOrCreateUser(packet.get("user"));

                    ServerMemberJoinEvent event = new ImplServerMemberJoinEvent(server, user);

                    List<ServerMemberJoinListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberJoinListeners());
                    listeners.addAll(user.getServerMemberJoinListeners());
                    listeners.addAll(api.getServerMemberJoinListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onServerMemberJoin(event));
                });
    }

}