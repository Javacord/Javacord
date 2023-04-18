package org.javacord.api.event.server.scheduledevent;

import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server scheduled event user remove event.
 */
public interface ServerScheduledEventUserRemoveEvent extends ServerEvent {

    /**
     * Gets the server scheduled event.
     *
     * @return The server scheduled event.
     */
    ServerScheduledEvent getServerScheduledEvent();

    /**
     * Gets the id of the user.
     *
     * @return The id of the user.
     */
    long getUserId();

}
