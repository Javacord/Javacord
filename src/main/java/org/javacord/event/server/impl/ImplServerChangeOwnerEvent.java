package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.server.ServerChangeOwnerEvent;

/**
 * The implementation of {@link ServerChangeOwnerEvent}.
 */
public class ImplServerChangeOwnerEvent extends ImplServerEvent implements ServerChangeOwnerEvent {

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
    public ImplServerChangeOwnerEvent(Server server, User newOwner, User oldOwner) {
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
