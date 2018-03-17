package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerBecomesUnavailableEvent;

/**
 * The implementation of {@link ServerBecomesUnavailableEvent}.
 */
public class ServerBecomesUnavailableEventImpl extends ServerEventImpl implements ServerBecomesUnavailableEvent {

    /**
     * Creates a new server becomes unavailable event.
     *
     * @param server The server of the event.
     */
    public ServerBecomesUnavailableEventImpl(Server server) {
        super(server);
    }

}
