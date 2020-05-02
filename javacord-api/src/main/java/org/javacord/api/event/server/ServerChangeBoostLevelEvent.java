package org.javacord.api.event.server;

import org.javacord.api.entity.server.BoostLevel;

/**
 * A server change boost level event.
 */
public interface ServerChangeBoostLevelEvent extends ServerEvent {

    /**
     * Gets the old boost level of the server.
     *
     * @return The old boost level of the server.
     */
    BoostLevel getOldBoostLevel();

    /**
     * Gets the new boost level of the server.
     *
     * @return The new boost level of the server.
     */
    BoostLevel getNewBoostLevel();

}
