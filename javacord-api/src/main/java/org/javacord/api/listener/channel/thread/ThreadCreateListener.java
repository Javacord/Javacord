package org.javacord.api.listener.channel.thread;

import org.javacord.api.event.channel.thread.ThreadCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ChannelThreadAttachableListener;

/**
 * This listener listens to thread creations.
 */
@FunctionalInterface
public interface ThreadCreateListener extends GloballyAttachableListener,
        ChannelThreadAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a thread is created.
     *
     * @param event The event.
     */
    void onThreadCreate(ThreadCreateEvent event);
}
