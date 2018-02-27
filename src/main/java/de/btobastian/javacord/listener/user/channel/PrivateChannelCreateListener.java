package de.btobastian.javacord.listener.user.channel;

import de.btobastian.javacord.event.channel.user.PrivateChannelCreateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
