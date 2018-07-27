package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.member.ServerMemberLeaveEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    User user = api.getOrCreateUser(packet.get("user"));
                    server.removeMember(user);
                    server.decrementMemberCount();

                    ServerMemberLeaveEvent event = new ServerMemberLeaveEventImpl(server, user);

                    List<ServerMemberLeaveListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberLeaveListeners());
                    listeners.addAll(user.getServerMemberLeaveListeners());
                    listeners.addAll(api.getServerMemberLeaveListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onServerMemberLeave(event));
                });
    }

}