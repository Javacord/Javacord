package de.btobastian.javacord.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.event.channel.text.WebhooksUpdateEvent;
import de.btobastian.javacord.listener.channel.text.WebhooksUpdateListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the webhooks update packet.
 */
public class WebhooksUpdateHandler extends PacketHandler {

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
        api.getServerTextChannelById(channelId).ifPresent(channel -> {
            WebhooksUpdateEvent event = new WebhooksUpdateEvent(channel);

            List<WebhooksUpdateListener> listeners = new ArrayList<>();
            listeners.addAll(channel.getWebhooksUpdateListeners());
            listeners.addAll(channel.getServer().getWebhooksUpdateListeners());
            listeners.addAll(api.getWebhooksUpdateListeners());

            api.getEventDispatcher()
                    .dispatchEvent(channel.getServer(), listeners, listener -> listener.onWebhooksUpdate(event));
        });
    }

}