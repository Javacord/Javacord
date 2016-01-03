package de.btobastian.javacord.permissions;

/**
 * A collection of all existing {@link PermissionType permission types}.
 */
public interface Permissions {

    /**
     * Gets the state of the given type.
     * 
     * @param type The type.
     * @return The state of the type.
     */
    public PermissionState getState(PermissionType type);
    
}
