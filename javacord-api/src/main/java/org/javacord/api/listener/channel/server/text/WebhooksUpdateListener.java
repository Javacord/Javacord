package org.javacord.api.listener.channel.server.text;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.text.WebhooksUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to webhook updates.
 * The event gets fired when a server channel's webhook is created, updated, or deleted.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_WEBHOOKS})
public interface WebhooksUpdateListener extends ServerAttachableListener, ServerTextChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time the webhooks of a channel get updated.
     *
     * @param event The event.
     */
    void onWebhooksUpdate(WebhooksUpdateEvent event);

}
