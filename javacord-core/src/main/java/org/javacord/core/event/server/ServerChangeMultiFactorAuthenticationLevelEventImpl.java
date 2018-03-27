package org.javacord.core.event.server;

import org.javacord.api.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;

/**
 * The implementation of {@link ServerChangeMultiFactorAuthenticationLevelEvent}.
 */
public class ServerChangeMultiFactorAuthenticationLevelEventImpl extends ServerEventImpl
        implements ServerChangeMultiFactorAuthenticationLevelEvent {

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
     * @param server The server of the event.
     * @param newMultiFactorAuthenticationLevel The new multi factor authentication level of the server.
     * @param oldMultiFactorAuthenticationLevel The old multi factor authentication level of the server.
     */
    public ServerChangeMultiFactorAuthenticationLevelEventImpl(
            Server server, MultiFactorAuthenticationLevel newMultiFactorAuthenticationLevel,
            MultiFactorAuthenticationLevel oldMultiFactorAuthenticationLevel) {
        super(server);
        this.newMultiFactorAuthenticationLevel = newMultiFactorAuthenticationLevel;
        this.oldMultiFactorAuthenticationLevel = oldMultiFactorAuthenticationLevel;
    }

    @Override
    public MultiFactorAuthenticationLevel getOldMultiFactorAuthenticationLevel() {
        return oldMultiFactorAuthenticationLevel;
    }

    @Override
    public MultiFactorAuthenticationLevel getNewMultiFactorAuthenticationLevel() {
        return newMultiFactorAuthenticationLevel;
    }

}
