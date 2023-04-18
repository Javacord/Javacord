package org.javacord.api.listener.server.scheduledevent;

import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUserAddEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server scheduled event user adds.
 */
@FunctionalInterface
public interface ServerScheduledEventUserAddListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server scheduled event user is added.
     *
     * @param event The event.
     */
    void onServerScheduledEventUserAdd(ServerScheduledEventUserAddEvent event);
}
