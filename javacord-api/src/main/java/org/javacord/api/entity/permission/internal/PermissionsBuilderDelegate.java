package org.javacord.api.entity.permission.internal;

import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;

/**
 * This class is internally used by the {@link PermissionsBuilder} to create permissions.
 * You usually don't want to interact with this object.
 */
public interface PermissionsBuilderDelegate {

    /**
     * Sets the new state of the given type.
     *
     * @param type The type to change.
     * @param state The state to set.
     */
    void setState(PermissionType type, PermissionState state);

    /**
     * Gets the state of the given type.
     *
     * @param type The type to check.
     * @return The state of the given type.
     */
    PermissionState getState(PermissionType type);

    /**
     * Creates a {@link Permissions} instance with the given values.
     *
     * @return The created permissions instance.
     */
    Permissions build();

}
