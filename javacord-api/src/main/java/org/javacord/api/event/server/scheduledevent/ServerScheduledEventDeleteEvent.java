package org.javacord.api.event.server.scheduledevent;

import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server scheduled event delete event.
 */
public interface ServerScheduledEventDeleteEvent extends ServerEvent {

    /**
     * Gets the deleted server scheduled event.
     *
     * @return The deleted server scheduled event.
     */
    ServerScheduledEvent getServerScheduledEvent();

}
