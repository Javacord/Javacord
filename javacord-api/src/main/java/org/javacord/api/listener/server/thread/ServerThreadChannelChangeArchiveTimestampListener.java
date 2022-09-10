package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchiveTimestampEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server thread channel archive timestamp change events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeArchiveTimestampListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread's archive timestamp changes.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeArchiveTimestamp(ServerThreadChannelChangeArchiveTimestampEvent event);
}
