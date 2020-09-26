package org.javacord.api.event.server;

import org.javacord.api.entity.user.User;

import java.util.Optional;

/**
 * A server change owner event.
 */
public interface ServerChangeOwnerEvent extends ServerEvent {

    /**
     * Gets the old owner of the server.
     *
     * @return The old owner of the server.
     */
    default Optional<User> getOldOwner() {
        return getApi().getCachedUserById(getOldOwnerId());
    }

    /**
     * Gets the id of the old owner of the server.
     * 
     * @return The old owner's id.
     */
    long getOldOwnerId();

    /**
     * Gets the new owner of the server.
     *
     * @return The new owner of the server.
     */
    default Optional<User> getNewOwner() {
        return getApi().getCachedUserById(getNewOwnerId());
    }

    /**
     * Gets the id of the new owner of the server.
     *
     * @return The new owner's id.
     */
    long getNewOwnerId();
}
