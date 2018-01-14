package de.btobastian.javacord.entities;

import java.util.Optional;

/**
 * This class represents a activity as it is displayed in Discord.
 */
public interface Activity {

    /**
     * Gets the type of the activity.
     *
     * @return The type of the activity.
     */
    ActivityType getType();

    /**
     * Gets the name of the activity.
     *
     * @return The name of the activity.
     */
    String getName();

    /**
     * Gets the streaming url of the activity.
     *
     * @return The streaming url of the activity.
     */
    Optional<String> getStreamingUrl();

}
