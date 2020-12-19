package org.javacord.api.command;

public enum ApplicationCommandOptionType {

    SUB_COMMAND(1),
    SUB_COMMAND_GROUP(2),
    STRING(3),
    INTEGER(4),
    BOOLEAN(5),
    USER(6),
    CHANNEL(7),
    ROLE(8),
    UNKNOWN(-1);

    private final int value;

    ApplicationCommandOptionType(int value) {
        this.value = value;
    }

    /**
     * Gets integer value that represents this type.
     *
     * @return The integer value that represents this type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets an {@code ApplicationCommandOptionType} by its value.
     *
     * @param value The value of the application command option type.
     * @return The application command option type for the given value,
     *         or {@link ApplicationCommandOptionType#UNKNOWN} if there's none with the given value.
     */
    public static ApplicationCommandOptionType fromValue(int value) {
        for (ApplicationCommandOptionType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
