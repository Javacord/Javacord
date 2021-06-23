package org.javacord.api.interaction;

public enum SlashCommandPermissionType {

    ROLE(1),
    USER(2),
    UNKNOWN(-1);

    private final int value;

    SlashCommandPermissionType(int value) {
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
     * Gets an {@code SlashCommandPermissionType} by its value.
     *
     * @param value The value of the slash command permission type.
     * @return The slash command permission type for the given value,
     *     or {@link SlashCommandPermissionType#UNKNOWN} if there's none with the given value.
     */
    public static SlashCommandPermissionType fromValue(int value) {
        for (SlashCommandPermissionType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
