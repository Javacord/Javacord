package org.javacord.api.listener.message;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.listener.webhook.WebhookAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to message creations.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MESSAGES, Intent.DIRECT_MESSAGES})
public interface MessageCreateListener extends ServerAttachableListener, UserAttachableListener,
        WebhookAttachableListener, TextChannelAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a message is created.
     *
     * @param event The event.
     */
    void onMessageCreate(MessageCreateEvent event);
}
