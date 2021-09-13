package org.javacord.api.interaction.callback;

/**
 * Represents an interaction callback data flag type.
 */
public enum InteractionCallbackDataFlag {

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
     * Creates a new interaction callback data flag type.
     *
     * @param id The id of the type.
     */
    InteractionCallbackDataFlag(final int id) {
        this.id = id;
    }

    /**
     * Gets the id of the interaction callback data flag type.
     *
     * @return The id of the interaction callback data flag type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the interaction callback data flag type by its id.
     *
     * @param id The id of the interaction callback data flag type.
     * @return The interaction callback data flag type with the given id
     *         or {@link InteractionCallbackDataFlag#UNKNOWN} if unknown id.
     */
    public static InteractionCallbackDataFlag getFlagTypeById(final int id) {
        for (final InteractionCallbackDataFlag value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
