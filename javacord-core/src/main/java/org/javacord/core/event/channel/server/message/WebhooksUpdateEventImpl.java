package org.javacord.core.event.channel.server.message;

import org.javacord.api.entity.channel.ServerMessageChannel;
import org.javacord.api.event.channel.server.message.WebhooksUpdateEvent;

/**
 * The implementation of {@link WebhooksUpdateEvent}.
 */
public class WebhooksUpdateEventImpl extends ServerMessageChannelEventImpl implements WebhooksUpdateEvent {

    /**
     * Creates a new webhooks update event.
     *
     * @param channel The channel of the event.
     */
    public WebhooksUpdateEventImpl(ServerMessageChannel channel) {
        super(channel);
    }

}
