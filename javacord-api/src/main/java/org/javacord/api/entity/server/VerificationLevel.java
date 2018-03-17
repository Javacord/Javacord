package org.javacord.api.entity.server;

/**
 * This enum contains all verification levels.
 */
public enum VerificationLevel {

    NONE(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    VERY_HIGH(4),
    UNKNOWN(-1);

    /**
     * The id of the verification level.
     */
    private final int id;

    /**
     * Creates a new verification level.
     *
     * @param id The id of the verification level.
     */
    VerificationLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the verification level.
     *
     * @return The id of the verification level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the verification level by its id.
     *
     * @param id The id of the verification level.
     * @return The verification level with the given id.
     */
    public static VerificationLevel fromId(int id) {
        for (VerificationLevel verificationLevel : values()) {
            if (verificationLevel.getId() == id) {
                return verificationLevel;
            }
        }
        return UNKNOWN;
    }
}
