package de.btobastian.javacord.listeners.user.channel;

import de.btobastian.javacord.events.user.channel.PrivateChannelDeleteEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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
