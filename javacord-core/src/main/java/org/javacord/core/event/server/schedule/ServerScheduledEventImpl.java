package org.javacord.core.event.server.schedule;

import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.schedule.ServerScheduledEvent;
import org.javacord.core.event.server.ServerEventImpl;

public class ServerScheduledEventImpl extends ServerEventImpl implements ServerScheduledEvent {
    /**
     * The scheduled event.
     */
    private final ScheduledEvent scheduledEvent;

    /**
     * Creates a new guild scheduled event.
     *
     * @param server         The server of the event.
     * @param scheduledEvent The scheduled event.
     */
    public ServerScheduledEventImpl(Server server, ScheduledEvent scheduledEvent) {
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
