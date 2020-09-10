package org.javacord.api.entity.server;

/**
 * An enum with all boost levels (sometimes also referred to as premium tier levels).
 */
public enum BoostLevel {

    /**
     * Server has no boost level.
     */
    NONE(0),
    /**
     * Server Boost level 1.
     */
    TIER_1(1),
    /**
     * Server Boost level 2.
     */
    TIER_2(2),

    /**
     * Server Boost level 3.
     */
    TIER_3(3),

    /**
     * An unknown boost level, most likely due to new added boost levels.
     */
    UNKNOWN(-1);


    /**
     * The id of the boost level.
     */
    private final int id;

    /**
     * Creates a new boost level.
     *
     * @param id The id of the boost level.
     */
    BoostLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the boost level.
     *
     * @return The id of the boost level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the boost level by its id.
     *
     * @param id The id of the boost level.
     * @return The boost level with the given id.
     */
    public static BoostLevel fromId(int id) {
        for (BoostLevel boostLevel : values()) {
            if (boostLevel.getId() == id) {
                return boostLevel;
            }
        }
        return UNKNOWN;
    }
}
