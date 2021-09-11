package org.javacord.api.listener.channel.thread;

import org.javacord.api.event.channel.thread.ThreadUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ChannelThreadAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to thread updates.
 */
@FunctionalInterface
public interface ThreadUpdateListener extends ServerAttachableListener, ChannelThreadAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a thread is updated.
     *
     * @param event The event.
     */
    void onThreadUpdate(ThreadUpdateEvent event);
}
