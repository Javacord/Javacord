package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerChangeOwnerEvent;

/**
 * The implementation of {@link ServerChangeOwnerEvent}.
 */
public class ServerChangeOwnerEventImpl extends ServerEventImpl implements ServerChangeOwnerEvent {

    /**
     * The new owner of the server.
     */
    private final User newOwner;

    /**
     * The old owner of the server.
     */
    private final User oldOwner;

    /**
     * Creates a new server change owner event.
     *
     * @param server The server of the event.
     * @param newOwner The new owner of the server.
     * @param oldOwner The old owner of the server.
     */
    public ServerChangeOwnerEventImpl(Server server, User newOwner, User oldOwner) {
        super(server);
        this.newOwner = newOwner;
        this.oldOwner = oldOwner;
    }

    @Override
    public User getOldOwner() {
        return oldOwner;
    }

    @Override
    public User getNewOwner() {
        return newOwner;
    }

}
