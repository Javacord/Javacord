package org.javacord.api.event.server.scheduledevent;

import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server scheduled event update event.
 */
public interface ServerScheduledEventUpdateEvent extends ServerEvent {

    /**
     * Gets the old server scheduled event.
     *
     * @return The old server scheduled event.
     */
    ServerScheduledEvent getOldServerScheduledEvent();

    /**
     * Gets the updated server scheduled event.
     *
     * @return The updated server scheduled event.
     */
    ServerScheduledEvent getServerScheduledEvent();

}
