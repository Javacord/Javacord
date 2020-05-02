package org.javacord.api.event.server;

import java.util.Optional;

/**
 * A server change description event.
 */
public interface ServerChangeDescriptionEvent extends ServerEvent {

    /**
     * Gets the old description of the server.
     *
     * @return The old description of the server.
     */
    Optional<String> getOldDescription();

    /**
     * Gets the new description of the server.
     *
     * @return The new description of the server.
     */
    Optional<String> getNewDescription();

}
