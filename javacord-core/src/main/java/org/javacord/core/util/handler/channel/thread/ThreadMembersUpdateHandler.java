package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.thread.ThreadMembersUpdateEvent;
import org.javacord.core.entity.channel.ChannelThreadImpl;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.thread.ThreadMembersUpdateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class ThreadMembersUpdateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ThreadListSyncHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadMembersUpdateHandler(DiscordApi api) {
        super(api, true, "THREAD_MEMBERS_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long channelId = packet.get("id").asLong();
        long serverId = packet.get("guild_id").asLong();
        ServerImpl server = api.getPossiblyUnreadyServerById(serverId).map(ServerImpl.class::cast).orElse(null);
        if (server == null) {
            logger.warn("Unable to find server with id {}", serverId);
            return;
        }
        ChannelThreadImpl thread = server.getChannelById(channelId).map(ChannelThreadImpl.class::cast).orElse(null);
        if (thread == null) {
            logger.warn("Unable to find thread with id {}", channelId);
            return;
        }

        int memberCount = packet.get("member_count").asInt();

        List<Long> removedMemberIds = new ArrayList<>();
        if (packet.has("removed_member_ids")) {
            for (JsonNode removedMemberId : packet.get("removed_member_ids")) {
                removedMemberIds.add(removedMemberId.asLong());
            }
        }

        List<ThreadMember> addedMembers = new ArrayList<>();
        if (packet.has("added_members")) {
            for (JsonNode addedMember : packet.get("added_members")) {
                addedMembers.add(new ThreadMemberImpl(addedMember));
            }
        }

        thread.setMembers(addedMembers);

        api.addChannelToCache(thread);

        ThreadMembersUpdateEvent event = new ThreadMembersUpdateEventImpl(thread, server, memberCount,
                addedMembers, removedMemberIds);

        api.getEventDispatcher().dispatchThreadMembersUpdateEvent((DispatchQueueSelector) server,
                thread, server, event);
    }
}
