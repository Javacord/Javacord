package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.thread.ThreadMembersUpdateEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.thread.ThreadMembersUpdateEventImpl;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;
import java.util.ArrayList;
import java.util.List;

public class ThreadMembersUpdateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ThreadMembersUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadMembersUpdateHandler(final DiscordApi api) {
        super(api, true, "THREAD_MEMBERS_UPDATE");
    }

    @Override
    public void handle(final JsonNode packet) {
        final long channelId = packet.get("id").asLong();
        final long serverId = packet.get("guild_id").asLong();
        final ServerImpl server = api.getServerById(serverId)
                .map(ServerImpl.class::cast)
                .orElse(null);

        if (server == null) {
            logger.warn("Unable to find server with id {}", serverId);
            return;
        }

        final ServerThreadChannelImpl thread = server.getThreadChannelById(channelId)
                .map(ServerThreadChannelImpl.class::cast)
                .orElse(null);

        if (thread == null) {
            logger.warn("Unable to find thread with id {}", channelId);
            return;
        }

        final int memberCount = packet.get("member_count").asInt();

        final List<Long> removedMemberIds = new ArrayList<>();
        if (packet.has("removed_member_ids")) {
            for (final JsonNode removedMemberId : packet.get("removed_member_ids")) {
                removedMemberIds.add(removedMemberId.asLong());
            }
        }

        final List<ThreadMember> addedMembers = new ArrayList<>();
        if (packet.has("added_members")) {
            for (final JsonNode addedMember : packet.get("added_members")) {
                addedMembers.add(new ThreadMemberImpl(api, server, addedMember));
            }
        }

        thread.setMembers(addedMembers);

        final ThreadMembersUpdateEvent event = new ThreadMembersUpdateEventImpl(thread, server, memberCount,
                addedMembers, removedMemberIds);

        api.getEventDispatcher().dispatchThreadMembersUpdateEvent(server, server, thread, event);
    }
}
