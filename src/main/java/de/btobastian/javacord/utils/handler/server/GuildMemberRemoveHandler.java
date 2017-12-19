package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerMemberRemoveEvent;
import de.btobastian.javacord.listeners.server.ServerMemberRemoveListener;
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

                    ServerMemberRemoveEvent event = new ServerMemberRemoveEvent(api, server, user);

                    List<ServerMemberRemoveListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberRemoveListeners());
                    listeners.addAll(user.getServerMemberRemoveListeners());
                    listeners.addAll(api.getServerMemberRemoveListeners());

                    dispatchEvent(listeners, listener -> listener.onServerMemberRemove(event));
                });
    }

}