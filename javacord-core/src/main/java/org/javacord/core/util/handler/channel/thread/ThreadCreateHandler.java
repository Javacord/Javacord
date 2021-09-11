package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelThread;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.channel.thread.ThreadCreateEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.thread.ThreadCreateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

/**
 * Handles the thread create packet.
 */
public class ThreadCreateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil
            .getLogger(org.javacord.core.util.handler.channel.thread.ThreadCreateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadCreateHandler(DiscordApi api) {
        super(api, true, "THREAD_CREATE");
    }

    @Override
    public void handle(JsonNode packet) {
        ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_PUBLIC_THREAD:
            case SERVER_PRIVATE_THREAD:
                handleThread(packet);
                break;
            default:
                logger.warn("Unknown or unexpected channel type. Your Javacord version might be out of date!");
        }
    }

    /**
     * Handles the creation of a thread.
     *
     * @param channel The channel data from which to build the thread.
     */
    private void handleThread(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ChannelThread textChannel = (ChannelThread)((ServerImpl) server).getOrCreateServerTextChannel(channel);
            ThreadCreateEvent event = new ThreadCreateEventImpl(textChannel);

            api.getEventDispatcher().dispatchThreadCreateEvent((DispatchQueueSelector) server, textChannel, event);
        });
    }
}
