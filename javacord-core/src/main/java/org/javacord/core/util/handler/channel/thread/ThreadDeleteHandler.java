package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelThread;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadDeleteEvent;
import org.javacord.core.event.channel.thread.ThreadDeleteEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;

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
    public ThreadDeleteHandler(DiscordApi api) {
        super(api, true, "THREAD_DELETE");
    }

    @Override
    public void handle(JsonNode packet) {
        ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_PUBLIC_THREAD:
                handleServerPublicThread(packet);
                break;
            case SERVER_PRIVATE_THREAD:
                handleServerPrivateThread(packet);
                break;
            default:
                logger.warn("Unknown or unexpected channel type. Your Javacord version might be out of date!");
        }
        api.removeChannelFromCache(packet.get("id").asLong());
    }

    /**
     * Handles the deletion of a server private thread.
     *
     * @param channelJson Json of the thread to be deleted.
     */
    private void handleServerPrivateThread(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getPossiblyUnreadyServerById(serverId)
                .flatMap(server -> server.getTextChannelById(channelId))
                .ifPresent(channel -> {
                    dispatchThreadDeleteEvent(channel);
                    api.forEachCachedMessageWhere(
                            msg -> msg.getChannel().getId() == channelId,
                            msg -> api.removeMessageFromCache(msg.getId())
                    );
                });
        api.removeObjectListeners(ChannelThread.class, channelId);
        api.removeObjectListeners(ServerTextChannel.class, channelId);
        api.removeObjectListeners(ServerChannel.class, channelId);
        api.removeObjectListeners(TextChannel.class, channelId);
        api.removeObjectListeners(Channel.class, channelId);
    }

    /**
     * Handles the deletion of a server private thread.
     *
     * @param channelJson Json of the thread to be deleted.
     */
    private void handleServerPublicThread(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getPossiblyUnreadyServerById(serverId)
                .flatMap(server -> server.getTextChannelById(channelId))
                .ifPresent(channel -> {
                    dispatchThreadDeleteEvent(channel);
                    api.forEachCachedMessageWhere(
                            msg -> msg.getChannel().getId() == channelId,
                            msg -> api.removeMessageFromCache(msg.getId())
                    );
                });
        api.removeObjectListeners(ChannelThread.class, channelId);
        api.removeObjectListeners(ServerTextChannel.class, channelId);
        api.removeObjectListeners(ServerChannel.class, channelId);
        api.removeObjectListeners(TextChannel.class, channelId);
        api.removeObjectListeners(Channel.class, channelId);
    }

    /**
     * Dispatches a thread delete event.
     *
     * @param thread The thread of the event.
     */
    private void dispatchThreadDeleteEvent(TextChannel thread) {
        ThreadDeleteEvent event = new ThreadDeleteEventImpl(thread);
        Optional<Server> optionalServer = thread.asServerChannel().map(ServerChannel::getServer);

        api.getEventDispatcher().dispatchThreadDeleteEvent(
                optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                (ChannelThread) thread,
                event);
    }
}
