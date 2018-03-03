package org.javacord.listener.message;

import org.javacord.event.message.MessageDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.message.MessageDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
