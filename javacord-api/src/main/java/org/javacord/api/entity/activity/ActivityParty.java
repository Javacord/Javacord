package org.javacord.api.entity.activity;

import java.util.Optional;

/**
 * This class represents an activity party.
 */
public interface ActivityParty {

    /**
     * Gets the id of the party.
     * This is not normal discord snowflake id!
     *
     * @return The id of the party.
     */
    Optional<String> getId();

    /**
     * Gets the current size of the party.
     *
     * @return The current size of the party.
     */
    Optional<Integer> getCurrentSize();

    /**
     * Gets the maximum size of the party.
     *
     * @return The maximum size of the party.
     */
    Optional<Integer> getMaximumSize();

}
