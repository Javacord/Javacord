package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerLeaveEvent;

/**
 * The implementation of {@link ServerLeaveEvent}.
 */
public class ServerLeaveEventImpl extends ServerEventImpl implements ServerLeaveEvent {

    /**
     * Creates a new server leave event.
     *
     * @param server The server of the event.
     */
    public ServerLeaveEventImpl(Server server) {
        super(server);
    }

}
