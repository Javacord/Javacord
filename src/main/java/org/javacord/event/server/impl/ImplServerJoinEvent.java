package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerJoinEvent;

/**
 * The implementation of {@link ServerJoinEvent}.
 */
public class ImplServerJoinEvent extends ImplServerEvent implements ServerJoinEvent {

    /**
     * Creates a new server join event.
     *
     * @param server The server of the event.
     */
    public ImplServerJoinEvent(Server server) {
        super(server);
    }

}
