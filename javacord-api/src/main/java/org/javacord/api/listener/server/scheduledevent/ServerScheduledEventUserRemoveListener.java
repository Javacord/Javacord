package org.javacord.api.listener.server.scheduledevent;

import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUserRemoveEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server scheduled events user removes.
 */
@FunctionalInterface
public interface ServerScheduledEventUserRemoveListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server scheduled event user is removed.
     *
     * @param event The event.
     */
    void onServerScheduledEventUserRemove(ServerScheduledEventUserRemoveEvent event);
}
