package org.javacord.api.command;

public enum InteractionResponseType {

    PONG(1),
    CHANNEL_MESSAGE_WITH_SOURCE(4),
    DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5),
    UNKNOWN(-1);

    private final int value;

    InteractionResponseType(int value) {
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
     * Gets an {@code InteractionResponseType} by its value.
     *
     * @param value The value of the interaction response type.
     * @return The interaction response type for the given value,
     *         or {@link InteractionResponseType#UNKNOWN} if there's none with the given value.
     */
    public static InteractionResponseType fromValue(int value) {
        for (InteractionResponseType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
