package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageDeleteEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
