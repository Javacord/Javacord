package org.javacord.api.entity.server;

import org.javacord.api.entity.user.User;

import java.util.Optional;

/**
 * This class represents a ban.
 */
public interface Ban {

    /**
     * Gets the server of the ban.
     *
     * @return The server of the ban.
     */
    Server getServer();

    /**
     * Gets the banned user.
     *
     * @return The banned user.
     */
    User getUser();

    /**
     * Gets the reason for the ban.
     *
     * @return The reason for the ban.
     */
    Optional<String> getReason();

}
