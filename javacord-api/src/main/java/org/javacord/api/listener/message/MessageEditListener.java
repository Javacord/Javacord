package org.javacord.api.listener.message;

import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
