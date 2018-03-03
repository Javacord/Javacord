package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.Server;

/**
 * A server change multi factor authentication level event.
 */
public class ServerChangeMultiFactorAuthenticationLevelEvent extends ServerEvent {

    /**
     * The new multi factor authentication level of the server.
     */
    private final MultiFactorAuthenticationLevel newMultiFactorAuthenticationLevel;

    /**
     * The old multi factor authentication level of the server.
     */
    private final MultiFactorAuthenticationLevel oldMultiFactorAuthenticationLevel;

    /**
     * Creates a new server change multi factor authentication level event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newMultiFactorAuthenticationLevel The new multi factor authentication level of the server.
     * @param oldMultiFactorAuthenticationLevel The old multi factor authentication level of the server.
     */
    public ServerChangeMultiFactorAuthenticationLevelEvent(
            DiscordApi api, Server server, MultiFactorAuthenticationLevel newMultiFactorAuthenticationLevel,
            MultiFactorAuthenticationLevel oldMultiFactorAuthenticationLevel) {
        super(api, server);
        this.newMultiFactorAuthenticationLevel = newMultiFactorAuthenticationLevel;
        this.oldMultiFactorAuthenticationLevel = oldMultiFactorAuthenticationLevel;
    }

    /**
     * Gets the old multi factor authentication level of the server.
     *
     * @return The old multi factor authentication level of the server.
     */
    public MultiFactorAuthenticationLevel getOldMultiFactorAuthenticationLevel() {
        return oldMultiFactorAuthenticationLevel;
    }

    /**
     * Gets the new multi factor authentication level of the server.
     *
     * @return The new multi factor authentication level of the server.
     */
    public MultiFactorAuthenticationLevel getNewMultiFactorAuthenticationLevel() {
        return newMultiFactorAuthenticationLevel;
    }

}
