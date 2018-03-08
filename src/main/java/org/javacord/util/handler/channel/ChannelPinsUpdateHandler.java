package org.javacord.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.message.ChannelPinsUpdateEvent;
import org.javacord.event.message.impl.ImplChannelPinsUpdateEvent;
import org.javacord.listener.message.ChannelPinsUpdateListener;
import org.javacord.util.gateway.PacketHandler;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the channel pins update packet.
 */
public class ChannelPinsUpdateHandler extends PacketHandler {

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
        api.getTextChannelById(packet.get("channel_id").asLong()).ifPresent(channel -> {
            Instant lastPinTimestamp = packet.hasNonNull("last_pin_timestamp") ?
                    OffsetDateTime.parse(packet.get("last_pin_timestamp").asText()).toInstant() : null;
            ChannelPinsUpdateEvent event = new ImplChannelPinsUpdateEvent(channel, lastPinTimestamp);

            List<ChannelPinsUpdateListener> listeners = new ArrayList<>();
            listeners.addAll(channel.getChannelPinsUpdateListeners());
            channel.asServerTextChannel()
                    .map(ServerTextChannel::getServer)
                    .map(Server::getChannelPinsUpdateListeners)
                    .ifPresent(listeners::addAll);
            listeners.addAll(api.getChannelPinsUpdateListeners());

            api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onChannelPinsUpdate(event));
        });
    }


}