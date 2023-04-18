package org.javacord.api.entity.server.scheduledevent;

public enum ServerScheduledEventStatus {

    SCHEDULED(1),
    ACTIVE(2),
    COMPLETED(3),
    CANCELED(4),
    UNKNOWN(-1);

    private final int value;

    ServerScheduledEventStatus(final int i) {
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
     * Gets an {@code EventStatus} by its value.
     *
     * @param value The value of the event status type.
     * @return The event status type for the given value,
     *         or {@link ServerScheduledEventStatus#UNKNOWN} if there's none with the given value.
     */
    public static ServerScheduledEventStatus fromValue(final int value) {
        for (final ServerScheduledEventStatus type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
