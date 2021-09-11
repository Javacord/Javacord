package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeSlowmodeEvent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import org.javacord.core.entity.channel.ChannelThreadImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeNameEventImpl;
import org.javacord.core.event.channel.server.text.ServerTextChannelChangeSlowmodeEventImpl;
import org.javacord.core.event.channel.server.text.ServerTextChannelChangeTopicEventImpl;
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
    public ThreadUpdateHandler(DiscordApi api) {
        super(api, true, "THREAD_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_PRIVATE_THREAD:
            case SERVER_PUBLIC_THREAD:
                handleThread(packet);
                break;
            default:
                logger.warn("Unknown or unexpected channel type. Your Javacord version might be out of date!");
        }
    }

    private void handleThread(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        long serverId = jsonChannel.get("guild_id").asLong();
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

        //Handling whether the name has changed
        String oldName = thread.getName();
        String newName = jsonChannel.get("name").asText();
        if (!Objects.deepEquals(oldName, newName)) {
            thread.setName(newName);
            ServerChannelChangeNameEvent event =
                    new ServerChannelChangeNameEventImpl(thread, newName, oldName);

            if (server.isReady()) {
                api.getEventDispatcher().dispatchServerChannelChangeNameEvent(
                        (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
            }
        }

        //Handling whether the topic has changed
        String oldTopic = thread.getTopic();
        String newTopic = jsonChannel.has("topic") && !jsonChannel.get("topic").isNull()
                ? jsonChannel.get("topic").asText() : "";
        if (!oldTopic.equals(newTopic)) {
            thread.setTopic(newTopic);

            ServerTextChannelChangeTopicEvent event =
                    new ServerTextChannelChangeTopicEventImpl(thread, newTopic, oldTopic);

            api.getEventDispatcher().dispatchServerTextChannelChangeTopicEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        //Handling whether the slowmode delay has changed
        int oldSlowmodeDelay = thread.getSlowmodeDelayInSeconds();
        int newSlowmodeDelay = jsonChannel.has("rate_limit_per_user")
                ? jsonChannel.get("rate_limit_per_user").asInt(0) : 0;
        if (oldSlowmodeDelay != newSlowmodeDelay) {
            thread.setSlowmodeDelayInSeconds(newSlowmodeDelay);
            ServerTextChannelChangeSlowmodeEvent event =
                    new ServerTextChannelChangeSlowmodeEventImpl(thread, oldSlowmodeDelay, newSlowmodeDelay);

            api.getEventDispatcher().dispatchServerTextChannelChangeSlowmodeEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event
            );
        }
    }
}
