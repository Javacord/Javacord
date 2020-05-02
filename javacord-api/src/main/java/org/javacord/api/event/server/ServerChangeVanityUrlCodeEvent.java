package org.javacord.api.event.server;

import org.javacord.api.entity.VanityUrlCode;

import java.util.Optional;

/**
 * A server change vanity url code event.
 */
public interface ServerChangeVanityUrlCodeEvent extends ServerEvent {

    /**
     * Gets the old vanity url code of the server.
     *
     * @return The old vanity url code of the server.
     */
    Optional<VanityUrlCode> getOldVanityUrlCode();

    /**
     * Gets the new vanity url code of the server.
     *
     * @return The new vanity url code of the server.
     */
    Optional<VanityUrlCode> getNewVanityUrlCode();

}
