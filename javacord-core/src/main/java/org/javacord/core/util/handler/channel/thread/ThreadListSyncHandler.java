package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelThread;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.thread.ThreadListSyncEvent;
import org.javacord.core.entity.channel.ChannelThreadImpl;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.thread.ThreadListSyncEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class ThreadListSyncHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ThreadListSyncHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadListSyncHandler(DiscordApi api) {
        super(api, true, "THREAD_LIST_SYNC");
    }

    @Override
    public void handle(JsonNode packet) {
        long serverId = packet.get("guild_id").asLong();
        ServerImpl server = api.getPossiblyUnreadyServerById(serverId).map(ServerImpl.class::cast).orElse(null);
        if (server == null) {
            logger.warn("Unable to find server with id {}", serverId);
            return;
        }

        List<Long> channelIds = new ArrayList<>();
        if (packet.has("channel_ids")) {
            for (JsonNode channelId : packet.get("channel_ids")) {
                channelIds.add(channelId.asLong());
            }
        }

        List<ChannelThread> threads = new ArrayList<>();
        for (JsonNode thread : packet.get("threads")) {
            threads.add(new ChannelThreadImpl(api, server, thread));
        }

        List<Long> threadIds = new ArrayList<>();
        for (ChannelThread thread : threads) {
            threadIds.add(thread.getId());
        }

        List<ThreadMember> members = new ArrayList<>();
        for (JsonNode member : packet.get("members")) {
            members.add(new ThreadMemberImpl(member));
        }

        for (ChannelThread thread : threads) {
            api.addChannelToCache(thread);
        }

        //Removes lost threads from cache
        for (Channel channel : api.getChannels()) {
            if (channel.getType() == ChannelType.SERVER_PRIVATE_THREAD
                    || channel.getType() == ChannelType.SERVER_PUBLIC_THREAD
                    && !threadIds.contains(channel.getId())) {
                api.removeChannelFromCache(channel.getId());
            }
        }

        ThreadListSyncEvent event = new ThreadListSyncEventImpl(server, channelIds, threads, members);

        api.getEventDispatcher().dispatchThreadListSyncEvent((DispatchQueueSelector) server, server, event);
    }
}
