package org.javacord.listener.channel.user;

import org.javacord.event.channel.user.PrivateChannelCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

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
