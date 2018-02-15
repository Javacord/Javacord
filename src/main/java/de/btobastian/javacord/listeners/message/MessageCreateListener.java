package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
