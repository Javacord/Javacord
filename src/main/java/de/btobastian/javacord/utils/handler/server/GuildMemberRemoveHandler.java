package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.member.ServerMemberLeaveEvent;
import de.btobastian.javacord.listeners.server.member.ServerMemberLeaveListener;
import de.btobastian.javacord.utils.PacketHandler;

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

                    dispatchEvent(listeners, listener -> listener.onServerMemberLeave(event));
                });
    }

}