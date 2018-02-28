package de.btobastian.javacord.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.channel.ServerTextChannel;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.event.message.ChannelPinsUpdateEvent;
import de.btobastian.javacord.listener.message.ChannelPinsUpdateListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

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
            ChannelPinsUpdateEvent event = new ChannelPinsUpdateEvent(channel, lastPinTimestamp);

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