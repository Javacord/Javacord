package org.javacord.api.listener.server.scheduledevent;

import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server scheduled event updates.
 */
@FunctionalInterface
public interface ServerScheduledEventUpdateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server scheduled event is updated.
     *
     * @param event The event.
     */
    void onServerScheduledEventUpdate(ServerScheduledEventUpdateEvent event);
}
