package org.javacord.listener.channel.text;

import org.javacord.event.channel.text.WebhooksUpdateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.channel.text.WebhooksUpdateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
