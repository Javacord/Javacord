package org.javacord.core.event.server.scheduledevent;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventCreateEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerScheduledEventCreateEvent}.
 */
public class ServerScheduledEventCreateEventImpl extends ServerEventImpl implements ServerScheduledEventCreateEvent {

    /**
     * The server scheduled event.
     */
    private final ServerScheduledEvent serverScheduledEvent;

    /**
     * Creates a new server scheduled event create event.
     *
     * @param server               The server of the event.
     * @param serverScheduledEvent The server scheduled event.
     */
    public ServerScheduledEventCreateEventImpl(Server server, ServerScheduledEvent serverScheduledEvent) {
        super(server);
        this.serverScheduledEvent = serverScheduledEvent;
    }

    @Override
    public ServerScheduledEvent getServerScheduledEvent() {
        return serverScheduledEvent;
    }
}
