package org.javacord.listener.user.channel;

import org.javacord.event.channel.user.PrivateChannelDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.channel.user.PrivateChannelDeleteEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to private channel deletions.
 */
@FunctionalInterface
public interface PrivateChannelDeleteListener extends UserAttachableListener, PrivateChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a private channel is deleted.
     *
     * @param event The event.
     */
    void onPrivateChannelDelete(PrivateChannelDeleteEvent event);

}
