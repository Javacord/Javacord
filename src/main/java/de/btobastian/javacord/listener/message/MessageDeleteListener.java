package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.event.message.MessageDeleteEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
