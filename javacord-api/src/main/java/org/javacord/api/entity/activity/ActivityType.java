package org.javacord.api.entity.activity;

/**
 * Represents a activity type.
 *
 * @see <a href="https://discord.com/developers/docs/topics/gateway#activity-object-activity-types">Discord docs</a>
 */
public enum ActivityType {

    /**
     * Represents a normal activity, represented as "Playing Half-Life 3" for example.
     */
    PLAYING(0),

    /**
     * Represents streaming a activity, represented as "Streaming Half-Life 3" for example.
     */
    STREAMING(1),

    /**
     * Represents listening to an application, represented as "Listening to Half-Life 3" for example.
     */
    LISTENING(2),

    /**
     * Represents watching an application, represented as "Watching Half-Life 3", for example.
     */
    WATCHING(3),

    /**
     * Represents a custom status, represented as "{emoji} {state}".
     */
    CUSTOM(4);

    private final int id;

    /**
     * Class constructor.
     *
     * @param id The id of the activity type
     */
    ActivityType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the activity type.
     *
     * @return The id of the activity type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the activity type by its id.
     *
     * @param id The id of the activity type
     * @return The activity type with the given id or {@link ActivityType#PLAYING} if unknown id.
     */
    public static ActivityType getActivityTypeById(int id) {
        switch (id) {
            case 0:
                return PLAYING;
            case 1:
                return STREAMING;
            case 2:
                return LISTENING;
            case 3:
                return WATCHING;
            case 4:
                return CUSTOM;
            default:
                return PLAYING;
        }
    }

}
