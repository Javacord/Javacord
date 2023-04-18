package org.javacord.api.entity.server.scheduledevent;

public enum ServerScheduledEventPrivacyLevel {

    SERVER_ONLY(2),
    UNKNOWN(-1);

    private final int value;

    ServerScheduledEventPrivacyLevel(final int i) {
        this.value = i;
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
     * Gets an {@code ServerScheduledEventPrivacyLevel} by its value.
     *
     * @param value The value of the privacy level type.
     * @return The privacy level type for the given value,
     *         or {@link ServerScheduledEventPrivacyLevel#UNKNOWN} if there's none with the given value.
     */
    public static ServerScheduledEventPrivacyLevel fromValue(final int value) {
        for (final ServerScheduledEventPrivacyLevel type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
