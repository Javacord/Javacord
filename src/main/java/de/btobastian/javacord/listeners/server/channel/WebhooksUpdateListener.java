package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.WebhooksUpdateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to webhook updates.
 * The event get's fired when a server channel's webhook is created, updated, or deleted.
 */
@FunctionalInterface
public interface WebhooksUpdateListener extends ServerAttachableListener, ServerTextChannelAttachableListener,
                                                GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time the webhooks of a channel get updated.
     *
     * @param event The event.
     */
    void onWebhooksUpdate(WebhooksUpdateEvent event);

}
