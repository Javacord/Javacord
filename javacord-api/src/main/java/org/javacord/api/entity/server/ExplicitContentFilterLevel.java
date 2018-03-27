package org.javacord.api.entity.server;

/**
 * This enum contains all explicit content filter levels.
 */
public enum ExplicitContentFilterLevel {

    DISABLED(0),
    MEMBERS_WITHOUT_ROLES(1),
    ALL_MEMBERS(2),
    UNKNOWN(-1);

    /**
     * The id of the explicit content filter level.
     */
    private final int id;

    /**
     * Creates a new explicit content filter level.
     *
     * @param id The id of the explicit content filter level.
     */
    ExplicitContentFilterLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the explicit content filter level.
     *
     * @return The id of the explicit content filter level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the explicit content filter level by its id.
     *
     * @param id The id of the explicit content filter level.
     * @return The explicit content filter level with the given id.
     */
    public static ExplicitContentFilterLevel fromId(int id) {
        for (ExplicitContentFilterLevel verificationLevel : values()) {
            if (verificationLevel.getId() == id) {
                return verificationLevel;
            }
        }
        return UNKNOWN;
    }
}
