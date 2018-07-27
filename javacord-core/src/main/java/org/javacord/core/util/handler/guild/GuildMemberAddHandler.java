package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.member.ServerMemberJoinEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    server.addMember(packet);
                    server.incrementMemberCount();
                    User user = api.getOrCreateUser(packet.get("user"));

                    ServerMemberJoinEvent event = new ServerMemberJoinEventImpl(server, user);

                    List<ServerMemberJoinListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberJoinListeners());
                    listeners.addAll(user.getServerMemberJoinListeners());
                    listeners.addAll(api.getServerMemberJoinListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onServerMemberJoin(event));
                });
    }

}