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

}
