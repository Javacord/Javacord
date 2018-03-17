package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerJoinEvent;

/**
 * The implementation of {@link ServerJoinEvent}.
 */
public class ServerJoinEventImpl extends ServerEventImpl implements ServerJoinEvent {

    /**
     * Creates a new server join event.
     *
     * @param server The server of the event.
     */
    public ServerJoinEventImpl(Server server) {
        super(server);
    }

}
