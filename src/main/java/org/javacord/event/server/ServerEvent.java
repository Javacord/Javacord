package org.javacord.event.server;

import org.javacord.entity.server.Server;
import org.javacord.event.Event;

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
