package de.btobastian.javacord.entities.permissions;

/**
 * A collection of all existing {@link PermissionType permission types} and their states.
 */
public interface Permissions {

    /**
     * Gets the state of the given type.
     *
     * @param type The type.
     * @return The state of the type.
     */
    PermissionState getState(PermissionType type);

    /**
     * Checks if the all permission types are set to {@link PermissionState#NONE}.
     *
     * @return Whether all permission types are set to NONE or not.
     */
    default boolean isEmpty() {
        for (PermissionType type : PermissionType.values()) {
            if (getState(type) != PermissionState.NONE) {
                return false;
            }
        }
        return true;
    }

}
