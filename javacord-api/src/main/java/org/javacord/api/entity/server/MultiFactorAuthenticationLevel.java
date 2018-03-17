package org.javacord.api.entity.server;

/**
 * This enum contains all multi factor authentication levels.
 */
public enum MultiFactorAuthenticationLevel {

    NONE(0),
    ELEVATED(1),
    UNKNOWN(-1);

    /**
     * The id of the multi factor authentication level.
     */
    private final int id;

    /**
     * Creates a new multi factor authentication level.
     *
     * @param id The id of the multi factor authentication level.
     */
    MultiFactorAuthenticationLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the multi factor authentication level.
     *
     * @return The id of the multi factor authentication level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the multi factor authentication level by its id.
     *
     * @param id The id of the multi factor authentication level.
     * @return The multi factor authentication level with the given id.
     */
    public static MultiFactorAuthenticationLevel fromId(int id) {
        for (MultiFactorAuthenticationLevel multiFactorAuthenticationLevel : values()) {
            if (multiFactorAuthenticationLevel.getId() == id) {
                return multiFactorAuthenticationLevel;
            }
        }
        return UNKNOWN;
    }

}
