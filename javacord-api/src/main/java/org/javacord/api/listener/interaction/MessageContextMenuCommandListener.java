package org.javacord.api.listener.interaction;

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to message context menu interaction creations.
 */
public interface MessageContextMenuCommandListener extends ServerAttachableListener, UserAttachableListener,
        MessageAttachableListener, TextChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a message context menu interaction is created.
     *
     * @param event The event.
     */
    void onMessageContextMenuCommand(MessageContextMenuCommandEvent event);
}
