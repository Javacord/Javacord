package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerBecomesUnavailableEvent;

/**
 * The implementation of {@link ServerBecomesUnavailableEvent}.
 */
public class ImplServerBecomesUnavailableEvent extends ImplServerEvent implements ServerBecomesUnavailableEvent {

    /**
     * Creates a new server becomes unavailable event.
     *
     * @param server The server of the event.
     */
    public ImplServerBecomesUnavailableEvent(Server server) {
        super(server);
    }

}
