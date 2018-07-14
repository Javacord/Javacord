package org.javacord.api.entity.message;

public enum MessageActivityType {

    /**
     * A join invite activity.
     */
    JOIN(1),

    /**
     * A spectate invite activity.
     */
    SPECTATE(2),

    /**
     * A listen invite activity.
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
     * The int representing the id.
     */
    final int id;

    /**
     * Creates a new activity id.
     *
     * @param id The int representation of the id.
     */
    MessageActivityType(int id) {
        this.id = id;
    }

    /**
     * Gets the id by its int representation.
     *
     * @param id The int representation.
     * @return The message id.
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
