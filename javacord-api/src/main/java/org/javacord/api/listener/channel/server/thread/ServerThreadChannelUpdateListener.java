package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.event.channel.thread.ThreadUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to thread updates.
 */
@FunctionalInterface
public interface ServerThreadChannelUpdateListener
        extends ServerAttachableListener, ServerThreadChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a thread is updated.
     *
     * @param event The event.
     */
    void onThreadUpdate(ThreadUpdateEvent event);
}
