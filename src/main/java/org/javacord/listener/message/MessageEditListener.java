package org.javacord.listener.message;

import org.javacord.event.message.MessageEditEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
