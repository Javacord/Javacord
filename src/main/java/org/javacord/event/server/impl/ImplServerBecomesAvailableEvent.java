package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerBecomesAvailableEvent;

/**
 * The implementation of {@link ServerBecomesAvailableEvent}.
 */
public class ImplServerBecomesAvailableEvent extends ImplServerEvent implements ServerBecomesAvailableEvent {

    /**
     * Creates a new server becomes available event.
     *
     * @param server The server of the event.
     */
    public ImplServerBecomesAvailableEvent(Server server) {
        super(server);
    }

}
