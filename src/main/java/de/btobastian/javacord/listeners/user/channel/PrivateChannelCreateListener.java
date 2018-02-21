package de.btobastian.javacord.listeners.user.channel;

import de.btobastian.javacord.events.user.channel.PrivateChannelCreateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

/**
 * This listener listens to private channel creations.
 */
@FunctionalInterface
public interface PrivateChannelCreateListener extends UserAttachableListener, GloballyAttachableListener,
                                                      ObjectAttachableListener {

    /**
     * This method is called every time a private channel is created.
     *
     * @param event The event.
     */
    void onPrivateChannelCreate(PrivateChannelCreateEvent event);
}
