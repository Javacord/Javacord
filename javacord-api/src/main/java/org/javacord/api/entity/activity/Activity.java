package org.javacord.api.entity.activity;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.emoji.Emoji;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * This class represents an activity as it is displayed in Discord.
 */
public interface Activity extends Nameable {

    /**
     * Gets the type of the activity.
     *
     * @return The type of the activity.
     */
    ActivityType getType();

    /**
     * Gets the streaming url of the activity.
     *
     * @return The streaming url of the activity.
     */
    Optional<String> getStreamingUrl();

    /**
     * Gets the time at which the activity was added/changed by the user.
     *
     * @return The time at which the activity was added/changed by the user.
     */
    Instant getCreatedAt();

    /**
     * Gets details about what's the user is currently doing.
     *
     * @return Details about what's the user is currently doing.
     */
    Optional<String> getDetails();

    /**
     * Gets the user's current party status.
     *
     * @return The user's current party status.
     */
    Optional<String> getState();

    /**
     * Gets the current party of the user.
     *
     * @return The current party of the user.
     */
    Optional<ActivityParty> getParty();

    /**
     * Gets the assets (images for the presence and their hover texts) of the activity.
     *
     * @return The assets of the activity.
     */
    Optional<ActivityAssets> getAssets();

    /**
     * Gets the secrets for Rich Presence joining and spectating.
     *
     * @return The secrets for Rich Presence joining and spectating.
     */
    Optional<ActivitySecrets> getSecrets();

    /**
     * Gets the application id of the game.
     *
     * @return The application id of the game.
     */
    Optional<Long> getApplicationId();

    /**
     * Gets the start time of the activity.
     *
     * @return The start time of the activity.
     */
    Optional<Instant> getStartTime();

    /**
     * Gets the end time of the activity.
     *
     * @return The end time of the activity.
     */
    Optional<Instant> getEndTime();

    /**
     * Gets the emoji of the custom status.
     *
     * @return The emoji of the custom status.
     */
    Optional<Emoji> getEmoji();

    /**
     * Whether the activity is an instanced game session.
     *
     * @return true if the activity is an instanced game session and false otherwise.
     */
    Optional<Boolean> getInstance();

    /**
     * Gets the activity flags of this activity.
     * The flags describe what the payload includes.
     *
     * @return the activity flags.
     */
    EnumSet<ActivityFlag> getFlags();

    /**
     * Gets the custom button labels shown in the Rich Presence (max 2).
     *
     * @return The custom button labels shown in the Rich Presence (max 2).
     */
    List<String> getButtonLabels();
}
