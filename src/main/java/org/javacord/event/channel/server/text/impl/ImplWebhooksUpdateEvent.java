package org.javacord.event.channel.server.text.impl;

import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.event.channel.server.text.WebhooksUpdateEvent;

/**
 * The implementation of {@link WebhooksUpdateEvent}.
 */
public class ImplWebhooksUpdateEvent extends ImplServerTextChannelEvent implements WebhooksUpdateEvent {

    /**
     * Creates a new webhooks update event.
     *
     * @param channel The channel of the event.
     */
    public ImplWebhooksUpdateEvent(ServerTextChannel channel) {
        super(channel);
    }

}
