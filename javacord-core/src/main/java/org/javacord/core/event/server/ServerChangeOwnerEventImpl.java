package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeOwnerEvent;

/**
 * The implementation of {@link ServerChangeOwnerEvent}.
 */
public class ServerChangeOwnerEventImpl extends ServerEventImpl implements ServerChangeOwnerEvent {

    /**
     * The id of the new owner of the server.
     */
    private final long newOwnerId;

    /**
     * The id of the old owner of the server.
     */
    private final long oldOwnerId;

    /**
     * Creates a new server change owner event.
     *
     * @param server The server of the event.
     * @param newOwnerId The id of the new owner of the server.
     * @param oldOwnerId The id of the old owner of the server.
     */
    public ServerChangeOwnerEventImpl(Server server, long newOwnerId, long oldOwnerId) {
        super(server);
        this.newOwnerId = newOwnerId;
        this.oldOwnerId = oldOwnerId;
    }

    @Override
    public long getOldOwnerId() {
        return oldOwnerId;
    }

    @Override
    public long getNewOwnerId() {
        return newOwnerId;
    }
}
