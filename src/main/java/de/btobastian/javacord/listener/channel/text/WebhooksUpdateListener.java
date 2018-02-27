package de.btobastian.javacord.listener.channel.text;

import de.btobastian.javacord.event.channel.text.WebhooksUpdateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
