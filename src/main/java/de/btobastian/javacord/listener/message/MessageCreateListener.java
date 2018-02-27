package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.event.message.MessageCreateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to message creations.
 */
@FunctionalInterface
public interface MessageCreateListener extends ServerAttachableListener, UserAttachableListener,
                                               TextChannelAttachableListener, GloballyAttachableListener,
                                               ObjectAttachableListener {

    /**
     * This method is called every time a message is created.
     *
     * @param event The event.
     */
    void onMessageCreate(MessageCreateEvent event);

}
