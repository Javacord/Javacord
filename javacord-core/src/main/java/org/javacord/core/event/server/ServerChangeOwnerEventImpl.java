package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerChangeOwnerEvent;

/**
 * The implementation of {@link ServerChangeOwnerEvent}.
 */
public class ServerChangeOwnerEventImpl extends ServerEventImpl implements ServerChangeOwnerEvent {

    /**
     * The id of the new owner of the server.
     */
    private final Long newOwnerId;

    /**
     * The old owner of the server.
     */
    private final User oldOwner;

    /**
     * Creates a new server change owner event.
     *
     * @param server The server of the event.
     * @param newOwnerId The id of the new owner of the server.
     * @param oldOwner The old owner of the server.
     */
    public ServerChangeOwnerEventImpl(Server server, Long newOwnerId, User oldOwner) {
        super(server);
        this.newOwnerId = newOwnerId;
        this.oldOwner = oldOwner;
    }

    @Override
    public User getOldOwner() {
        return oldOwner;
    }

    @Override
    public User getNewOwner() {
        // server related events should only get dispatched after all members are cached
        return getApi().getCachedUserById(newOwnerId).orElseThrow(AssertionError::new);
    }

}
