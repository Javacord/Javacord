package org.javacord.api.listener.message;

import org.javacord.api.event.message.MessageReplyEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.listener.webhook.WebhookAttachableListener;

/**
 * This listener listens to message replies.
 */
@FunctionalInterface
public interface MessageReplyListener extends ServerAttachableListener, UserAttachableListener,
        WebhookAttachableListener, TextChannelAttachableListener, MessageAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called when a message is created which replies to another.
     *
     * @param event The event.
     */
    void onMessageReply(MessageReplyEvent event);
}
