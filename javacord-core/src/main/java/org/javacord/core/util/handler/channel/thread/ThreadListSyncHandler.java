package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.thread.ThreadListSyncEvent;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.thread.ThreadListSyncEventImpl;
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
    public ThreadListSyncHandler(final DiscordApi api) {
        super(api, true, "THREAD_LIST_SYNC");
    }

    @Override
    public void handle(final JsonNode packet) {
        final long serverId = packet.get("guild_id").asLong();
        final ServerImpl server = api.getServerById(serverId)
                .map(ServerImpl.class::cast)
                .orElse(null);
        if (server == null) {
            logger.warn("Unable to find server with id {}", serverId);
            return;
        }

        final List<Long> channelIds = new ArrayList<>();
        if (packet.has("channel_ids")) {
            for (final JsonNode channelId : packet.get("channel_ids")) {
                channelIds.add(channelId.asLong());
            }
        }

        final List<ServerThreadChannel> threads = new ArrayList<>();
        for (final JsonNode thread : packet.get("threads")) {
            threads.add(server.getOrCreateServerThreadChannel(thread));
        }

        final List<Long> threadIds = new ArrayList<>();
        for (final ServerThreadChannel thread : threads) {
            threadIds.add(thread.getId());
        }

        final List<ThreadMember> members = new ArrayList<>();
        for (final JsonNode member : packet.get("members")) {
            members.add(new ThreadMemberImpl(api, server, member));
        }

        //Removes lost threads from cache
        for (final Channel channel : api.getChannels()) {
            if (channel.getType() == ChannelType.SERVER_PRIVATE_THREAD
                    || channel.getType() == ChannelType.SERVER_PUBLIC_THREAD
                    && !threadIds.contains(channel.getId())) {
                api.removeChannelFromCache(channel.getId());
            }
        }

        final ThreadListSyncEvent event = new ThreadListSyncEventImpl(server, channelIds, threads, members);

        api.getEventDispatcher().dispatchThreadListSyncEvent(server, server, event);
    }
}
