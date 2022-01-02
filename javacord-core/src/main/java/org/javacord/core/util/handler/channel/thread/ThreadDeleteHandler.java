package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadDeleteEvent;
import org.javacord.core.event.channel.thread.ThreadDeleteEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

/**
 * Handles the thread delete packet.
 */
public class ThreadDeleteHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(
            org.javacord.core.util.handler.channel.thread.ThreadDeleteHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadDeleteHandler(final DiscordApi api) {
        super(api, true, "THREAD_DELETE");
    }

    @Override
    public void handle(final JsonNode packet) {
        final ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_PUBLIC_THREAD:
            case SERVER_PRIVATE_THREAD:
            case SERVER_NEWS_THREAD:
                handleServerThread(packet);
                break;
            default:
                logger.warn("Unknown or unexpected channel type. Your Javacord version might be out of date!");
        }
    }

    private void handleServerThread(final JsonNode packet) {
        final long serverId = packet.get("guild_id").asLong();
        final long channelId = packet.get("id").asLong();
        api.getPossiblyUnreadyServerById(serverId)
                .flatMap(server -> server.getThreadChannelById(channelId))
                .ifPresent(channel -> {
                    dispatchThreadDeleteEvent(channel);
                    api.forEachCachedMessageWhere(
                            msg -> msg.getChannel().getId() == channelId,
                            msg -> api.removeMessageFromCache(msg.getId())
                    );
                });
        api.removeObjectListeners(ServerThreadChannel.class, channelId);
        api.removeObjectListeners(ServerChannel.class, channelId);
        api.removeObjectListeners(TextChannel.class, channelId);
        api.removeObjectListeners(Channel.class, channelId);

        api.removeChannelFromCache(packet.get("id").asLong());
    }

    /**
     * Dispatches a thread delete event.
     *
     * @param serverThreadChannel The thread of the event.
     */
    private void dispatchThreadDeleteEvent(final ServerThreadChannel serverThreadChannel) {
        final ThreadDeleteEvent event = new ThreadDeleteEventImpl(serverThreadChannel);
        final Server server = serverThreadChannel.getServer();

        api.getEventDispatcher().dispatchThreadDeleteEvent((DispatchQueueSelector) server, serverThreadChannel, event);
    }
}
