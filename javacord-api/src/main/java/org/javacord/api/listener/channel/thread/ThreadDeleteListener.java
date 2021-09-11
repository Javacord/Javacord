package org.javacord.api.listener.channel.thread;

import org.javacord.api.event.channel.thread.ThreadDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ChannelThreadAttachableListener;

/**
 * This listener listens to thread deletions.
 */
@FunctionalInterface
public interface ThreadDeleteListener extends ChannelThreadAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a thread is deleted.
     *
     * @param event The event.
     */
    void onThreadDelete(ThreadDeleteEvent event);
}
