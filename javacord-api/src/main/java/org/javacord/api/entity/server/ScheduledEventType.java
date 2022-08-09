package org.javacord.api.entity.server;

import java.util.Arrays;

public enum ScheduledEventType {
    UNKNOWN(-1),
    STAGE_INSTANCE(1),
    VOICE(2),
    EXTERNAL(3);

    /**
     * The id of the Event Type.
     */
    private final int id;

    /**
     * Creates a new Event Type.
     *
     * @param id The id of the Event Type.
     */
    ScheduledEventType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the Event Type.
     *
     * @return The id  of the Event Type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the Event Type by id.
     * @param id The id of the Event Type.
     * @return The Event Type with the given id.
     */
    public static ScheduledEventType fromId(int id) {
        return Arrays.stream(values())
                .filter(scheduledEventType -> scheduledEventType.getId() == id)
                .findFirst().orElse(UNKNOWN);
    }
}
