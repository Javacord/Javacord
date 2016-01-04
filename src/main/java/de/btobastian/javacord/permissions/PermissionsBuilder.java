package de.btobastian.javacord.permissions;

/**
 * Helps to build a {@link Permissions} instance.
 */
public interface PermissionsBuilder {

    /**
     * Sets the state of the given type.
     * 
     * @param type The type to change.
     * @param state The state to set.
     */
    public void setState(PermissionType type, PermissionState state);
    
    /**
     * Gets the state of the given type.
     * 
     * @param type The type to check.
     * @return The state of the given type.
     */
    public PermissionState getState(PermissionType type);
    
    /**
     * Creates a {@link Permissions} instance with the given values.
     * 
     * @return The created permissions instance.
     */
    public Permissions build();
    
}
