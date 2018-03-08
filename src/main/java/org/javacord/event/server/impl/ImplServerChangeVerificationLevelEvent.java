package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.event.server.ServerChangeVerificationLevelEvent;

/**
 * The implementation of {@link ServerChangeVerificationLevelEvent}.
 */
public class ImplServerChangeVerificationLevelEvent extends ImplServerEvent
        implements ServerChangeVerificationLevelEvent {

    /**
     * The new verification level of the server.
     */
    private final VerificationLevel newVerificationLevel;

    /**
     * The old verification level of the server.
     */
    private final VerificationLevel oldVerificationLevel;

    /**
     * Creates a new server change name event.
     *
     * @param server The server of the event.
     * @param newVerificationLevel The new verification level of the server.
     * @param oldVerificationLevel The old verification level of the server.
     */
    public ImplServerChangeVerificationLevelEvent(
            Server server, VerificationLevel newVerificationLevel, VerificationLevel oldVerificationLevel) {
        super(server);
        this.newVerificationLevel = newVerificationLevel;
        this.oldVerificationLevel = oldVerificationLevel;
    }

    @Override
    public VerificationLevel getOldVerificationLevel() {
        return oldVerificationLevel;
    }

    @Override
    public VerificationLevel getNewVerificationLevel() {
        return newVerificationLevel;
    }

}
