package de.btobastian.javacord.event.channel.text;

import de.btobastian.javacord.entity.channel.ServerTextChannel;
import de.btobastian.javacord.event.channel.server.text.ServerTextChannelEvent;

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
