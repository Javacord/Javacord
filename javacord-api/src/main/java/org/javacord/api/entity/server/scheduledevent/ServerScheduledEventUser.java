package org.javacord.api.entity.server.scheduledevent;

import org.javacord.api.entity.user.User;
import java.util.Optional;

/**
 * This class represents a server scheduled event user.
 */
public interface ServerScheduledEventUser {

    /**
     * Gets the server scheduled event.
     *
     * @return The server scheduled event.
     */
    ServerScheduledEvent getServerScheduledEvent();

    /**
     * Gets the user.
     *
     * @return The user.
     */
    User getUser();

    /**
     * Gets the member.
     *
     * @return The member.
     */
    Optional<User> getMember();

}
