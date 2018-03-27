package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerBecomesAvailableEvent;

/**
 * The implementation of {@link ServerBecomesAvailableEvent}.
 */
public class ServerBecomesAvailableEventImpl extends ServerEventImpl implements ServerBecomesAvailableEvent {

    /**
     * Creates a new server becomes available event.
     *
     * @param server The server of the event.
     */
    public ServerBecomesAvailableEventImpl(Server server) {
        super(server);
    }

}
