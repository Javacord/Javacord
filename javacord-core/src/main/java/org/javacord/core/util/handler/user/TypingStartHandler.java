package org.javacord.core.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.server.member.ServerMemberUserStartTypingEvent;
import org.javacord.core.entity.member.MemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.member.ServerMemberUserStartTypingEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the typing start packet.
 */
public class TypingStartHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public TypingStartHandler(DiscordApi api) {
        super(api, true, "TYPING_START");
    }

    @Override
    public void handle(JsonNode packet) {
        long userId = packet.get("user_id").asLong();
        long channelId = packet.get("channel_id").asLong();
        TextChannel channel = api.getTextChannelById(channelId).orElse(null);

        ServerImpl server = null;
        if (packet.hasNonNull("guild_id")) {
            long serverId = packet.get("guild_id").asLong();
            server = (ServerImpl) api.getPossiblyUnreadyServerById(serverId).orElseThrow(AssertionError::new);
        }

        MemberImpl member = null;
        if (packet.hasNonNull("member") && server != null) {
            member = new MemberImpl(api, server, packet.get("member"), null);
        }

        if (channel != null) {
            ServerMemberUserStartTypingEvent event = new ServerMemberUserStartTypingEventImpl(channel, userId, member);
            api.getEventDispatcher().dispatchServerMemberUserStartTypingEvent(
                    server != null ? server : api,
                    server,
                    member,
                    channel,
                    userId,
                    event);
        }
    }

}
