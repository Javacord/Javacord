package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.event.channel.thread.ThreadMembersUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to thread members updates.
 */
@FunctionalInterface
public interface ServerThreadChannelMembersUpdateListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener, ServerThreadChannelAttachableListener {

    /**
     * This method is called every time someone is added to or removed from a thread.
     *
     * @param event The event.
     */
    void onThreadMembersUpdate(ThreadMembersUpdateEvent event);
}
