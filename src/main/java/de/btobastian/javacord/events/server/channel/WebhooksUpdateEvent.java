package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerTextChannel;

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
