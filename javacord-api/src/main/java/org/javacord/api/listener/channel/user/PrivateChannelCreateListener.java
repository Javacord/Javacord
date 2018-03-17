package org.javacord.api.listener.channel.user;

import org.javacord.api.event.channel.user.PrivateChannelCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

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
