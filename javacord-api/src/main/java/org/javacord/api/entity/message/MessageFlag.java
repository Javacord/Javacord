package org.javacord.api.entity.message;

/**
 * Represents a message flag type.
 */
public enum MessageFlag {

    /**
     * An ephemeral message (only visible for the user).
     */
    EPHEMERAL(64),

    /**
     * An unknown flag.
     */
    UNKNOWN(-1);

    /**
     * The id of the flag.
     */
    private final int id;

    /**
     * Creates a new message flag type.
     *
     * @param id The id of the type.
     */
    MessageFlag(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the message flag type.
     *
     * @return The id of the message flag type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the message flag type by its id.
     *
     * @param id The id of the message flag type.
     * @return The message flag type with the given id or {@link MessageFlag#UNKNOWN} if unknown id.
     */
    public static MessageFlag getFlagTypeById(int id) {
        switch (id) {
            case 64:
                return EPHEMERAL;
            default:
                return UNKNOWN;
        }
    }
}
