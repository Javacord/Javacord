package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMessageCountEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to a server thread channel message count change events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeMessageCountListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {
    /**
     * This method is called every time a server thread's message count changes.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeMessageCount(ServerThreadChannelChangeMessageCountEvent event);
}
