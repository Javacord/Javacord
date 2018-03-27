package org.javacord.api.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.Event;

/**
 * A server event.
 */
public interface ServerEvent extends Event {

    /**
     * Gets the server of the event.
     *
     * @return The server of the event.
     */
    Server getServer();

}
