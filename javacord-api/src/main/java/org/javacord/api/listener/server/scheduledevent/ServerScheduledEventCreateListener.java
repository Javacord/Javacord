package org.javacord.api.listener.server.scheduledevent;

import org.javacord.api.event.server.scheduledevent.ServerScheduledEventCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server scheduled event creations.
 */
@FunctionalInterface
public interface ServerScheduledEventCreateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server scheduled event is created.
     *
     * @param event The event.
     */
    void onServerScheduledEventCreate(ServerScheduledEventCreateEvent event);
}
