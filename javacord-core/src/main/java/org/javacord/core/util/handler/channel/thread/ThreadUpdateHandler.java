package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeNameEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;
import java.util.Objects;

public class ThreadUpdateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ThreadUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadUpdateHandler(final DiscordApi api) {
        super(api, true, "THREAD_UPDATE");
    }

    @Override
    public void handle(final JsonNode packet) {
        final ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_PUBLIC_THREAD:
            case SERVER_PRIVATE_THREAD:
            case SERVER_NEWS_THREAD:
                handleThread(packet);
                break;
            default:
                logger.warn("Unknown or unexpected channel type. Your Javacord version might be out of date!");
        }
    }

    private void handleThread(final JsonNode jsonChannel) {
        final long channelId = jsonChannel.get("id").asLong();
        final long serverId = jsonChannel.get("guild_id").asLong();
        final ServerImpl server = api.getServerById(serverId)
                .map(ServerImpl.class::cast)
                .orElse(null);

        if (server == null) {
            logger.warn("Unable to find server with id {}", serverId);
            return;
        }

        final ServerThreadChannelImpl thread = 
                (ServerThreadChannelImpl) server.getOrCreateServerThreadChannel(jsonChannel);

        //Handling whether the name has changed
        final String oldName = thread.getName();
        final String newName = jsonChannel.get("name").asText();
        if (!Objects.deepEquals(oldName, newName)) {
            thread.setName(newName);
            final ServerChannelChangeNameEvent event =
                    new ServerChannelChangeNameEventImpl(thread, newName, oldName);

            if (server.isReady()) {
                api.getEventDispatcher().dispatchServerChannelChangeNameEvent(
                        (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
            }
        }

    }
}
