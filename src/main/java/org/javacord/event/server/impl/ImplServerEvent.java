package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.impl.ImplEvent;
import org.javacord.event.server.ServerEvent;

/**
 * The implementation of {@link ServerEvent}.
 */
public abstract class ImplServerEvent extends ImplEvent implements ServerEvent {

    /**
     * The server of the event.
     */
    private final Server server;

    /**
     * Creates a new server event.
     *
     * @param server The server of the event.
     */
    public ImplServerEvent(Server server) {
        super(server.getApi());
        this.server = server;
    }

    @Override
    public Server getServer() {
        return server;
    }

}
