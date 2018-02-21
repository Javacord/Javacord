package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageEditEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to message edits.
 */
@FunctionalInterface
public interface MessageEditListener extends ServerAttachableListener, TextChannelAttachableListener,
                                             MessageAttachableListener, GloballyAttachableListener,
                                             ObjectAttachableListener {

    /**
     * This method is called every time a message is edited.
     *
     * @param event The event.
     */
    void onMessageEdit(MessageEditEvent event);
}
