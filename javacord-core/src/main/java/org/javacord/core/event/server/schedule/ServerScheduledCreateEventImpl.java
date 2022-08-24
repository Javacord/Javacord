package org.javacord.core.event.server.schedule;

import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.schedule.ServerScheduledCreateEvent;
import org.javacord.core.event.server.ServerEventImpl;

public class ServerScheduledCreateEventImpl extends ServerEventImpl implements ServerScheduledCreateEvent {
    /**
     * The scheduled event.
     */
    private final ScheduledEvent scheduledEvent;

    /**
     * Creates a new guild scheduled event create event.
     *
     * @param server The server of the event.
     * @param scheduledEvent The scheduled event.
     */
    public ServerScheduledCreateEventImpl(Server server, ScheduledEvent scheduledEvent) {
        super(server);
        this.scheduledEvent = scheduledEvent;
    }

    @Override
    public ScheduledEvent getScheduledEvent() {
        return scheduledEvent;
    }

    @Override
    public Server getServer() {
        return scheduledEvent.getServer();
    }
}
