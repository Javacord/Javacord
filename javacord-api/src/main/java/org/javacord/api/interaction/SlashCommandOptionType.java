package org.javacord.api.interaction;

public enum SlashCommandOptionType {

    SUB_COMMAND(1),
    SUB_COMMAND_GROUP(2),
    STRING(3),
    /**
     * This is implemented as a long because it can be any integer between -2^53 and 2^53 and
     * therefore exceeds Javas Integer range. This is the INTEGER option according to the Discord docs.
     */
    LONG(4),
    BOOLEAN(5),
    USER(6),
    CHANNEL(7),
    ROLE(8),
    MENTIONABLE(9),
    /**
     * Any double between -2^53 and 2^53. This is the NUMBER option according to the Discord docs.
     */
    DECIMAL(10),
    UNKNOWN(-1);

    private final int value;

    SlashCommandOptionType(int value) {
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
     * Gets an {@code SlashCommandOptionType} by its value.
     *
     * @param value The value of the slash command option type.
     * @return The slash command option type for the given value,
     *         or {@link SlashCommandOptionType#UNKNOWN} if there's none with the given value.
     */
    public static SlashCommandOptionType fromValue(int value) {
        for (SlashCommandOptionType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
