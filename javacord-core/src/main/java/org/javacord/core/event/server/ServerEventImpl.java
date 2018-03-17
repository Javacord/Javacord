package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link ServerEvent}.
 */
public abstract class ServerEventImpl extends EventImpl implements ServerEvent {

    /**
     * The server of the event.
     */
    private final Server server;

    /**
     * Creates a new server event.
     *
     * @param server The server of the event.
     */
    public ServerEventImpl(Server server) {
        super(server.getApi());
        this.server = server;
    }

    @Override
    public Server getServer() {
        return server;
    }

}
