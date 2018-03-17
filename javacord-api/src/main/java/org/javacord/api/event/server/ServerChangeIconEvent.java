package org.javacord.api.event.server;

import org.javacord.api.entity.Icon;

import java.util.Optional;

/**
 * A server change icon event.
 */
public interface ServerChangeIconEvent extends ServerEvent {

    /**
     * Gets the old icon of the server.
     *
     * @return The old icon of the server.
     */
    Optional<Icon> getOldIcon();

    /**
     * Gets the new icon of the server.
     *
     * @return The new icon of the server.
     */
    Optional<Icon> getNewIcon();

}
