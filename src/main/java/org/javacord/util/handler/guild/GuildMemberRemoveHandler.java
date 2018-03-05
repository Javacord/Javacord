package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.event.server.member.ServerMemberLeaveEvent;
import org.javacord.listener.server.member.ServerMemberLeaveListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

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
        api.getServerById(packet.get("guild_id").asText())
                .map(server -> (ImplServer) server)
                .ifPresent(server -> {
                    User user = api.getOrCreateUser(packet.get("user"));
                    server.removeMember(user);
                    server.decrementMemberCount();

                    ServerMemberLeaveEvent event = new ServerMemberLeaveEvent(api, server, user);

                    List<ServerMemberLeaveListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberLeaveListeners());
                    listeners.addAll(user.getServerMemberLeaveListeners());
                    listeners.addAll(api.getServerMemberLeaveListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onServerMemberLeave(event));
                });
    }

}