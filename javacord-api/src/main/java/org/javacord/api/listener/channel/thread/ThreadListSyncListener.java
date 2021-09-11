package org.javacord.api.listener.channel.thread;

import org.javacord.api.event.channel.thread.ThreadListSyncEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to thread list syncs.
 */
@FunctionalInterface
public interface ThreadListSyncListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a user gains access to a channel.
     *
     * @param event The event.
     */
    void onThreadListSync(ThreadListSyncEvent event);
}
