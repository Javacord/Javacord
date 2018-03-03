package org.javacord.entity.permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
     * Gets a collection with permission types which are set to ({@link PermissionState#ALLOWED}).
     *
     * @return A collection with all allowed permissions.
     */
    default Collection<PermissionType> getAllowedPermission() {
        return Collections.unmodifiableCollection(Arrays.stream(PermissionType.values())
                .filter(type -> getState(type) == PermissionState.ALLOWED)
                .collect(Collectors.toSet()));
    }

    /**
     * Gets a collection with permission types which are set to ({@link PermissionState#DENIED}).
     *
     * @return A collection with all denied permissions.
     */
    default Collection<PermissionType> getDeniedPermissions() {
        return Collections.unmodifiableCollection(Arrays.stream(PermissionType.values())
                .filter(type -> getState(type) == PermissionState.DENIED)
                .collect(Collectors.toSet()));
    }

    /**
     * Gets a collection with permission types which are set to ({@link PermissionState#NONE}).
     *
     * @return A collection with all unset permissions.
     */
    default Collection<PermissionType> getUnsetPermissions() {
        return Collections.unmodifiableCollection(Arrays.stream(PermissionType.values())
                .filter(type -> getState(type) == PermissionState.NONE)
                .collect(Collectors.toSet()));
    }

    /**
     * Checks if the all permission types are set to {@link PermissionState#NONE}.
     *
     * @return Whether all permission types are set to NONE or not.
     */
    boolean isEmpty(); // We could check it in a default method, but it's faster to just check it in the implementation

}
