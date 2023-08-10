package org.javacord.core.event.server.scheduledevent;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUserAddEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerScheduledEventUserAddEvent}.
 */
public class ServerScheduledEventUserAddEventImpl extends ServerEventImpl implements ServerScheduledEventUserAddEvent {

    /**
     * The server scheduled event.
     */
    private final ServerScheduledEvent serverScheduledEvent;

    /**
     * The id of the user.
     */
    private final long userId;

    /**
     * Creates a new server scheduled event user add event.
     *
     * @param server The server of the event.
     * @param serverScheduledEvent The server scheduled event.
     * @param userId The id of the user.
     */
    public ServerScheduledEventUserAddEventImpl(Server server, ServerScheduledEvent serverScheduledEvent, long userId) {
        super(server);
        this.serverScheduledEvent = serverScheduledEvent;
        this.userId = userId;
    }

    @Override
    public ServerScheduledEvent getServerScheduledEvent() {
        return serverScheduledEvent;
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
