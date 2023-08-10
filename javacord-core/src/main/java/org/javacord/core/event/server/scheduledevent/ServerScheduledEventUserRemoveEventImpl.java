package org.javacord.core.event.server.scheduledevent;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.event.server.scheduledevent.ServerScheduledEventUserRemoveEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerScheduledEventUserRemoveEvent}.
 */
public class ServerScheduledEventUserRemoveEventImpl extends ServerEventImpl
        implements ServerScheduledEventUserRemoveEvent {

    /**
     * The server scheduled event.
     */
    private final ServerScheduledEvent serverScheduledEvent;

    /**
     * The id of the user.
     */
    private final long userId;

    /**
     * Creates a new server scheduled event user remove event.
     *
     * @param server The server of the event.
     * @param serverScheduledEvent The server scheduled event.
     * @param userId The id of the user.
     */
    public ServerScheduledEventUserRemoveEventImpl(Server server, ServerScheduledEvent serverScheduledEvent,
                                                   long userId) {
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
