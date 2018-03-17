package org.javacord.api.event.server;

import org.javacord.api.entity.server.VerificationLevel;

/**
 * A server change verification level event.
 */
public interface ServerChangeVerificationLevelEvent extends ServerEvent {

    /**
     * Gets the old verification level of the server.
     *
     * @return The old verification level of the server.
     */
    VerificationLevel getOldVerificationLevel();

    /**
     * Gets the new verification level of the server.
     *
     * @return The new verification level of the server.
     */
    VerificationLevel getNewVerificationLevel();

}
