package org.javacord.api.interaction;

public enum ApplicationCommandType {

    /**
     * This is not an official type and can be any application command type.
     * This is also used as a fallback for new types.
     */
    APPLICATION_COMMAND(0),
    SLASH(1),
    USER(2),
    MESSAGE(3),
    UNKNOWN(-1);

    private final int type;
    ApplicationCommandType(int type) {
        this.type = type;
    }

    /**
     * Gets integer value that represents this type.
     *
     * @return The integer value that represents this type.
     */
    public int getValue() {
        return type;
    }

    /**
     * Gets an {@code CommandType} by its value.
     *
     * @param value The value of the command type.
     * @return The command type for the given value,
     *         or {@link ApplicationCommandType#UNKNOWN} if there's no types with the given value.
     */
    public static ApplicationCommandType fromValue(int value) {
        for (ApplicationCommandType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
