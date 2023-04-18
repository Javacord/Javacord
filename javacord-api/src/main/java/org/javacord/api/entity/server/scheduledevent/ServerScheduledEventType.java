package org.javacord.api.entity.server.scheduledevent;

public enum ServerScheduledEventType {

    STAGE_INSTANCE(1),
    VOICE(2),
    EXTERNAL(3),
    UNKNOWN(-1);

    private final int value;

    ServerScheduledEventType(final int i) {
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
     * Gets an {@code EntityType} by its value.
     *
     * @param value The value of the entity type.
     * @return The entity type for the given value,
     *         or {@link ServerScheduledEventType#UNKNOWN} if there's none with the given value.
     */
    public static ServerScheduledEventType fromValue(final int value) {
        for (final ServerScheduledEventType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
