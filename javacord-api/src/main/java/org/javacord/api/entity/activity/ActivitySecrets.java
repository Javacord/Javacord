package org.javacord.api.entity.activity;

import java.util.Optional;

public interface ActivitySecrets {
    /**
     * Gets the secret for joining a party.
     *
     * @return The secret for joining a party.
     */
    Optional<String> getJoin();

    /**
     * Gets the secret for spectating a party.
     *
     * @return The secret for spectating a party.
     */
    Optional<String> getSpectate();

    /**
     * Gets the secret for a specific instanced match.
     *
     * @return The secret for a specific instanced match.
     */
    Optional<String> getMatch();
}
