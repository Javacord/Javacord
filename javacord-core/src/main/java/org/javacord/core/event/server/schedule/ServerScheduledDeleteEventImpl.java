package org.javacord.core.event.server.schedule;

import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.schedule.ServerScheduledDeleteEvent;

public class ServerScheduledDeleteEventImpl extends ServerScheduledEventImpl implements ServerScheduledDeleteEvent {
    /**
     * Creates a new guild scheduled event.
     *
     * @param server         The server of the event.
     * @param scheduledEvent The scheduled event.
     */
    public ServerScheduledDeleteEventImpl(Server server, ScheduledEvent scheduledEvent) {
        super(server, scheduledEvent);
    }
}
