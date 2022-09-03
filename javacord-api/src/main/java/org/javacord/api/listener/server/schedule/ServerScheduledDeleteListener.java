package org.javacord.api.listener.server.schedule;

import org.javacord.api.event.server.schedule.ServerScheduledDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * The listener listens to server scheduled events.
 */
public interface ServerScheduledDeleteListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a scheduled event is deleted.
     *
     * @param event The event.
     */
    void onServerScheduledDelete(ServerScheduledDeleteEvent event);
}
