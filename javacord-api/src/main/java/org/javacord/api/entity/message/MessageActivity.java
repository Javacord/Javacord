package org.javacord.api.entity.message;

import java.util.Optional;

/**
 * This class represents a MessageActivity like Spotify group listening.
 */
public interface MessageActivity {

    /**
     * Gets the type of the activity.
     *
     * @return The type of the activity.
     */
    MessageActivityType getType();

    /**
     * Gets the party id of the activity.
     *
     * @return The party id of the activity.
     */
    Optional<String> getPartyId();

    /**
     * Gets the message of the activity.
     *
     * @return The message of the activity.
     */
    Message getMessage();
}
