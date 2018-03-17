package org.javacord.core.event.channel.server.text;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.server.text.WebhooksUpdateEvent;

/**
 * The implementation of {@link WebhooksUpdateEvent}.
 */
public class WebhooksUpdateEventImpl extends ServerTextChannelEventImpl implements WebhooksUpdateEvent {

    /**
     * Creates a new webhooks update event.
     *
     * @param channel The channel of the event.
     */
    public WebhooksUpdateEventImpl(ServerTextChannel channel) {
        super(channel);
    }

}
