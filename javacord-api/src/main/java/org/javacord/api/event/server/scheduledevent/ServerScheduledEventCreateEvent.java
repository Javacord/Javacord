package org.javacord.api.event.server.scheduledevent;

import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server scheduled event create event.
 */
public interface ServerScheduledEventCreateEvent extends ServerEvent {

    /**
     * Gets the created server scheduled event.
     *
     * @return The created server scheduled event.
     */
    ServerScheduledEvent getServerScheduledEvent();

}
