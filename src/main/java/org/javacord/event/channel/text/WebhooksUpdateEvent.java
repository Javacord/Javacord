package org.javacord.event.channel.text;

import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.event.channel.server.text.ServerTextChannelEvent;

/**
 * A webhooks update event.
 * It get's fired when a server channel's webhook is created, updated, or deleted.
 */
public class WebhooksUpdateEvent extends ServerTextChannelEvent {

    /**
     * Creates a new webhooks update event.
     *
     * @param channel The channel of the event.
     */
    public WebhooksUpdateEvent(ServerTextChannel channel) {
        super(channel);
    }

}
