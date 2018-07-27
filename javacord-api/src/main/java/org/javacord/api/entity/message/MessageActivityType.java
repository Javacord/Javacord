package org.javacord.api.entity.message;

/**
 * Represents a message activity type.
 */
public enum MessageActivityType {

    /**
     * A join activity.
     */
    JOIN(1),

    /**
     * A spectate activity.
     */
    SPECTATE(2),

    /**
     * A listen activity.
     */
    LISTEN(3),

    /**
     * A join request activity.
     */
    JOIN_REQUEST(5),

    /**
     * An unknown activity.
     */
    UNKNOWN(-1);

    /**
     * The id of the type.
     */
    private final int id;

    /**
     * Creates a new message activity type.
     *
     * @param id The id of the type.
     */
    MessageActivityType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the message activity type.
     *
     * @return The id of the message activity type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the message activity type by its id.
     *
     * @param id The id of the message activity type.
     * @return The message activity type with the given id or {@link MessageActivityType#UNKNOWN} if unknown id.
     */
    public static MessageActivityType getMessageActivityTypeById(int id) {
        switch (id) {
            case 1:
                return JOIN;
            case 2:
                return SPECTATE;
            case 3:
                return LISTEN;
            case 5:
                return JOIN_REQUEST;
            default:
                return UNKNOWN;
        }
    }
}
