package org.javacord.core.event.server;

import org.javacord.api.event.server.ServerChangeBoostCountEvent;
import org.javacord.core.entity.server.ServerImpl;

/**
 * The implementation of {@link ServerChangeBoostCountEvent}.
 */
public class ServerChangeBoostCountEventImpl extends ServerEventImpl implements ServerChangeBoostCountEvent {

    /**
     * The old boost count of the server.
     */
    private final int oldBoostCount;

    /**
     * The new boost count of the server.
     */
    private final int newBoostCount;

    /**
     * Creates a new boost count change event.
     *
     * @param server        The server of the event.
     * @param newBoostCount The new boost count of the server.
     * @param oldBoostCount The old boost count of the server.
     */
    public ServerChangeBoostCountEventImpl(ServerImpl server, int newBoostCount, int oldBoostCount) {
        super(server);
        this.oldBoostCount = oldBoostCount;
        this.newBoostCount = newBoostCount;
    }

    @Override
    public int getOldBoostCount() {
        return oldBoostCount;
    }

    @Override
    public int getNewBoostCount() {
        return newBoostCount;
    }
}
