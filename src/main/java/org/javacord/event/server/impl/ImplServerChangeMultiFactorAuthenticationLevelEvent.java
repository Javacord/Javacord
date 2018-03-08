package org.javacord.event.server.impl;

import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;

/**
 * The implementation of {@link ServerChangeMultiFactorAuthenticationLevelEvent}.
 */
public class ImplServerChangeMultiFactorAuthenticationLevelEvent extends ImplServerEvent
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
    public ImplServerChangeMultiFactorAuthenticationLevelEvent(
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
