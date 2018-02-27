package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.event.message.MessageEditEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
