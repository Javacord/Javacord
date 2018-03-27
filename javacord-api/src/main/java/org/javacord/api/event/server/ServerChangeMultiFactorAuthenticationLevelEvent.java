package org.javacord.api.event.server;

import org.javacord.api.entity.server.MultiFactorAuthenticationLevel;

/**
 * A server change multi factor authentication level event.
 */
public interface ServerChangeMultiFactorAuthenticationLevelEvent extends ServerEvent {

    /**
     * Gets the old multi factor authentication level of the server.
     *
     * @return The old multi factor authentication level of the server.
     */
    MultiFactorAuthenticationLevel getOldMultiFactorAuthenticationLevel();

    /**
     * Gets the new multi factor authentication level of the server.
     *
     * @return The new multi factor authentication level of the server.
     */
    MultiFactorAuthenticationLevel getNewMultiFactorAuthenticationLevel();

}
