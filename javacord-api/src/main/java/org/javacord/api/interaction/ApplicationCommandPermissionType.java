package org.javacord.api.interaction;

public enum ApplicationCommandPermissionType {

    ROLE(1),
    USER(2),
    UNKNOWN(-1);

    private final int value;

    ApplicationCommandPermissionType(int value) {
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
     * Gets an {@code ApplicationCommandPermissionType} by its value.
     *
     * @param value The value of the application command permission type.
     * @return The application command permission type for the given value,
     *     or {@link ApplicationCommandPermissionType#UNKNOWN} if there's none with the given value.
     */
    public static ApplicationCommandPermissionType fromValue(int value) {
        for (ApplicationCommandPermissionType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
