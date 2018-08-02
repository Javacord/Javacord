package org.javacord.api.listener.message;

import org.javacord.api.event.message.MessageDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to message deletions.
 */
@FunctionalInterface
public interface MessageDeleteListener extends ServerAttachableListener, TextChannelAttachableListener,
                                               MessageAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a message is deleted.
     *
     * @param event The event.
     */
    void onMessageDelete(MessageDeleteEvent event);
}
