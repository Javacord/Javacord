package org.javacord.api.listener.channel.server.text;

import org.javacord.api.event.channel.server.text.WebhooksUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
