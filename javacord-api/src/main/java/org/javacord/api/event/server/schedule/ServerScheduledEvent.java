package org.javacord.api.event.server.schedule;

import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerEvent;

public interface ServerScheduledEvent extends ServerEvent {
    /**
     * Gets the scheduled event.
     *
     * @return The scheduled event.
     */
    ScheduledEvent getScheduledEvent();

    @Override
    default Server getServer() {
        return getScheduledEvent().getServer();
    }
}
