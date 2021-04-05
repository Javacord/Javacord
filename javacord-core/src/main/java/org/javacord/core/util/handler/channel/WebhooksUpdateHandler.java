package org.javacord.core.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.server.text.WebhooksUpdateEvent;
import org.javacord.core.event.channel.server.text.WebhooksUpdateEventImpl;
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
        if (optionalChannel.isPresent()) {
            ServerTextChannel channel = optionalChannel.get();
            WebhooksUpdateEvent event = new WebhooksUpdateEventImpl(channel);

            api.getEventDispatcher().dispatchWebhooksUpdateEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        } else {
            LoggerUtil.logMissingChannel(logger, channelId);
        }
    }

}
