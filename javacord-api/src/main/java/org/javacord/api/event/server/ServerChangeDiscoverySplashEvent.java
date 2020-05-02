package org.javacord.api.event.server;

import org.javacord.api.entity.Icon;

import java.util.Optional;

/**
 * A server change discovery splash event.
 */
public interface ServerChangeDiscoverySplashEvent extends ServerEvent {

    /**
     * Gets the old discovery splash of the server.
     *
     * @return The old discovery splash of the server.
     */
    Optional<Icon> getOldDiscoverySplash();

    /**
     * Gets the new discovery splash of the server.
     *
     * @return The new discovery splash of the server.
     */
    Optional<Icon> getNewDiscoverySplash();

}
