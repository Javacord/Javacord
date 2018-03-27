package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.event.server.ServerChangeVerificationLevelEvent;

/**
 * The implementation of {@link ServerChangeVerificationLevelEvent}.
 */
public class ServerChangeVerificationLevelEventImpl extends ServerEventImpl
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
    public ServerChangeVerificationLevelEventImpl(
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
