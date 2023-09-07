package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.event.channel.thread.ThreadJoinEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;

/**
 * This listener listens to thread joins.
 */
@FunctionalInterface
public interface ServerThreadChannelJoinListener extends GloballyAttachableListener,
        ServerThreadChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a bot joins a thread.
     *
     * @param event The event.
     */
    void onThreadJoin(ThreadJoinEvent event);
}
