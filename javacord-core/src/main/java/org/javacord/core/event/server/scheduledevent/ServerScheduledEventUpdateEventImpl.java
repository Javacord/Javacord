package org.javacord.core.event.server.scheduledevent;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUpdateEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerScheduledEventUpdateEvent}.
 */
public class ServerScheduledEventUpdateEventImpl extends ServerEventImpl implements ServerScheduledEventUpdateEvent {

    /**
     * The server scheduled event.
     */
    private final ServerScheduledEvent serverScheduledEvent;

    /**
     * The old server scheduled event.
     */
    private final ServerScheduledEvent oldServerScheduledEvent;

    /**
     * Creates a new server scheduled event update event.
     *
     * @param server               The server of the event.
     * @param oldServerScheduledEvent The old server scheduled event.
     * @param serverScheduledEvent The server scheduled event.
     */
    public ServerScheduledEventUpdateEventImpl(Server server, ServerScheduledEvent oldServerScheduledEvent,
                                               ServerScheduledEvent serverScheduledEvent) {
        super(server);
        this.oldServerScheduledEvent = oldServerScheduledEvent;
        this.serverScheduledEvent = serverScheduledEvent;
    }

    @Override
    public ServerScheduledEvent getOldServerScheduledEvent() {
        return oldServerScheduledEvent;
    }

    @Override
    public ServerScheduledEvent getServerScheduledEvent() {
        return serverScheduledEvent;
    }
}
