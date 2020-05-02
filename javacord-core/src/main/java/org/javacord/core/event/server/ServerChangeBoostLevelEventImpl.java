package org.javacord.core.event.server;

import org.javacord.api.entity.server.BoostLevel;
import org.javacord.api.event.server.ServerChangeBoostLevelEvent;
import org.javacord.core.entity.server.ServerImpl;

/**
 * The implementation of {@link ServerChangeBoostLevelEvent}.
 */
public class ServerChangeBoostLevelEventImpl extends ServerEventImpl implements ServerChangeBoostLevelEvent {

    /**
     * The old boost level of the server.
     */
    private final BoostLevel oldBoostLevel;

    /**
     * The new boost level of the server.
     */
    private final BoostLevel newBoostLevel;

    /**
     * Creates a new boost level change event.
     *
     * @param server The server of the event.
     * @param newBoostLevel The new boost level of the server.
     * @param oldBoostLevel The old boost level of the server.
     */
    public ServerChangeBoostLevelEventImpl(ServerImpl server, BoostLevel newBoostLevel, BoostLevel oldBoostLevel) {
        super(server);
        this.oldBoostLevel = oldBoostLevel;
        this.newBoostLevel = newBoostLevel;
    }

    @Override
    public BoostLevel getOldBoostLevel() {
        return oldBoostLevel;
    }

    @Override
    public BoostLevel getNewBoostLevel() {
        return newBoostLevel;
    }
}
