package de.btobastian.javacord.listener.user.channel;

import de.btobastian.javacord.event.channel.user.PrivateChannelDeleteEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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
