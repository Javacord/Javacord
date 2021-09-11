package org.javacord.api.listener.channel.thread;

import org.javacord.api.event.channel.thread.ThreadMembersUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ChannelThreadAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to thread members updates.
 */
@FunctionalInterface
public interface ThreadMembersUpdateListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener, ChannelThreadAttachableListener {

    /**
     * This method is called every time someone is added to or removed from a thread.
     *
     * @param event The event.
     */
    void onThreadMembersUpdate(ThreadMembersUpdateEvent event);
}
