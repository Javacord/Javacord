package org.javacord.api.entity.server;

import java.util.Arrays;

public enum ScheduledEventStatus {
    UNKNOWN(-1),
    SCHEDULED(1),
    ACTIVE(2),
    COMPLETED(3),
    CANCELED(4);

    /**
     * The id of the Event Status.
     */
    private final int id;

    /**
     * Creates a new Event Status.
     *
     * @param id The id of the Event Status.
     */
    ScheduledEventStatus(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the Event Status.
     *
     * @return The id  of the Event Status.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the Event Status by id.
     * @param id The id of the Event Status.
     * @return The Event Status with the given id.
     */
    public static ScheduledEventStatus fromId(int id) {
        return Arrays.stream(values())
                .filter(scheduledEventStatus -> scheduledEventStatus.getId() == id)
                .findFirst().orElse(UNKNOWN);
    }
}
