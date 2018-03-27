package org.javacord.api.event.server;

import org.javacord.api.entity.Icon;

import java.util.Optional;

/**
 * A server change splash event.
 */
public interface ServerChangeSplashEvent extends ServerEvent {

    /**
     * Gets the old splash of the server.
     *
     * @return The old splash of the server.
     */
    Optional<Icon> getOldSplash();

    /**
     * Gets the new splash of the server.
     *
     * @return The new splash of the server.
     */
    Optional<Icon> getNewSplash();

}
