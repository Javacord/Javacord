package org.javacord.core.event.channel.server.text;

import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.event.channel.server.text.WebhooksUpdateEvent;
import org.javacord.core.event.channel.server.textable.TextableRegularServerChannelEventImpl;

/**
 * The implementation of {@link WebhooksUpdateEvent}.
 */
public class WebhooksUpdateEventImpl extends TextableRegularServerChannelEventImpl implements WebhooksUpdateEvent {

    /**
     * Creates a new webhooks update event.
     *
     * @param channel The channel of the event.
     */
    public WebhooksUpdateEventImpl(TextableRegularServerChannel channel) {
        super(channel);
    }

}
