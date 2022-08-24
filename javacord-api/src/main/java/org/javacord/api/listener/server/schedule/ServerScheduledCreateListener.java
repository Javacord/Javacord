package org.javacord.api.listener.server.schedule;

import org.javacord.api.event.server.schedule.ServerScheduledCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to scheduled event creations.
 */
public interface ServerScheduledCreateListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a scheduled event is created.
     *
     * @param event The event.
     */
    void onServerScheduledCreate(ServerScheduledCreateEvent event);
}
