package org.javacord.core.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.ChannelPinsUpdateEvent;
import org.javacord.core.event.message.ChannelPinsUpdateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Handles the channel pins update packet.
 */
public class ChannelPinsUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ChannelPinsUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelPinsUpdateHandler(DiscordApi api) {
        super(api, true, "CHANNEL_PINS_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long channelId = packet.get("channel_id").asLong();
        Optional<TextChannel> optionalChannel = api.getTextChannelById(channelId);
        if (optionalChannel.isPresent()) {
            TextChannel channel = optionalChannel.get();
            Instant lastPinTimestamp = packet.hasNonNull("last_pin_timestamp")
                    ? OffsetDateTime.parse(packet.get("last_pin_timestamp").asText()).toInstant() : null;
            ChannelPinsUpdateEvent event = new ChannelPinsUpdateEventImpl(channel, lastPinTimestamp);

            Optional<Server> optionalServer = channel.asServerChannel().map(ServerChannel::getServer);
            api.getEventDispatcher().dispatchChannelPinsUpdateEvent(
                    optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                    optionalServer.orElse(null),
                    channel,
                    event);
        } else {
            LoggerUtil.logMissingChannel(logger, channelId);
        }
    }

}
