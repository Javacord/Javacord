package org.javacord.api.listener.server.scheduledevent;

import org.javacord.api.event.server.scheduledevent.ServerScheduledEventDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server scheduled event deletes.
 */
@FunctionalInterface
public interface ServerScheduledEventDeleteListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server scheduled event is deleted.
     *
     * @param event The event.
     */
    void onServerScheduledEventDelete(ServerScheduledEventDeleteEvent event);
}
