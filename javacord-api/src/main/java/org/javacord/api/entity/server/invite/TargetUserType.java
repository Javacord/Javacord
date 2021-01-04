package org.javacord.api.entity.server.invite;

/**
 * An enum with all target user types.
 */
public enum TargetUserType {

    STREAM(1),
    UNKNOWN(-1);

    /**
     * The id of the target user type.
     */
    private final int id;

    /**
     * Creates a new target user type.
     *
     * @param id The id of the target user type.
     */
    TargetUserType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the target user type.
     *
     * @return The id of the target user type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the target user type by its id.
     *
     * @param id The id of the target user type.
     * @return The target user type with the given id.
     */
    public static TargetUserType fromId(int id) {
        for (TargetUserType targetUserType : values()) {
            if (targetUserType.getId() == id) {
                return targetUserType;
            }
        }
        return UNKNOWN;
    }
}