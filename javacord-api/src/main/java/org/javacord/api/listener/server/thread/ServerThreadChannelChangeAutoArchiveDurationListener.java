package org.javacord.api.listener.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeAutoArchiveDurationEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server thread channel auto archive duration change events.
 */
@FunctionalInterface
public interface ServerThreadChannelChangeAutoArchiveDurationListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread's auto archive duration changed.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeAutoArchiveDuration(ServerThreadChannelChangeAutoArchiveDurationEvent event);
}
