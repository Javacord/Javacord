package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerLeaveEvent;

/**
 * The implementation of {@link ServerLeaveEvent}.
 */
public class ImplServerLeaveEvent extends ImplServerEvent implements ServerLeaveEvent {

    /**
     * Creates a new server leave event.
     *
     * @param server The server of the event.
     */
    public ImplServerLeaveEvent(Server server) {
        super(server);
    }

}
