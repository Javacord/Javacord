package org.javacord.core.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerNewsChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.server.message.WebhooksUpdateEvent;
import org.javacord.core.event.channel.server.message.WebhooksUpdateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;

/**
 * Handles the webhooks update packet.
 */
public class WebhooksUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(WebhooksUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public WebhooksUpdateHandler(DiscordApi api) {
        super(api, true, "WEBHOOKS_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long channelId = packet.get("channel_id").asLong();
        Optional<ServerTextChannel> optionalChannel =  api.getServerTextChannelById(channelId);
        Optional<ServerNewsChannel> optionalNewsChannel = api.getServerNewsChannelById(channelId);
        if (optionalChannel.isPresent()) {
            ServerTextChannel channel = optionalChannel.get();
            WebhooksUpdateEvent event = new WebhooksUpdateEventImpl(channel);

            api.getEventDispatcher().dispatchWebhooksUpdateEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), null,
                    channel, event);
        } else if (optionalNewsChannel.isPresent()) {
            ServerNewsChannel channel = optionalNewsChannel.get();
            WebhooksUpdateEvent event = new WebhooksUpdateEventImpl(channel);

            api.getEventDispatcher().dispatchWebhooksUpdateEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel,
                    null, event);
        } else {
            LoggerUtil.logMissingChannel(logger, channelId);
        }
    }

}
