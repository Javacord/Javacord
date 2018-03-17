package org.javacord.api.entity.server;

/**
 * This enum contains all default message notification levels.
 */
public enum DefaultMessageNotificationLevel {

    ALL_MESSAGES(0),
    ONLY_MENTIONS(1),
    UNKNOWN(-1);

    /**
     * The id of the default message notification level.
     */
    private final int id;

    /**
     * Creates a new default message notification level.
     *
     * @param id The id of the default message notification level.
     */
    DefaultMessageNotificationLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the default message notification level.
     *
     * @return The id of the default message notification level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the default message notification level by its id.
     *
     * @param id The id of the default message notification level.
     * @return The default message notification level with the given id.
     */
    public static DefaultMessageNotificationLevel fromId(int id) {
        for (DefaultMessageNotificationLevel verificationLevel : values()) {
            if (verificationLevel.getId() == id) {
                return verificationLevel;
            }
        }
        return UNKNOWN;
    }
}
