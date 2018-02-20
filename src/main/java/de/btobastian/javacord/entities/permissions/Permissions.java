package de.btobastian.javacord.entities.permissions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

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
        Collection<PermissionType> types = new HashSet<>();
        for (PermissionType type : PermissionType.values()) {
            if (getState(type) == PermissionState.ALLOWED) {
                types.add(type);
            }
        }
        return Collections.unmodifiableCollection(types);
    }

    /**
     * Gets a collection with permission types which are set to ({@link PermissionState#DENIED}).
     *
     * @return A collection with all denied permissions.
     */
    default Collection<PermissionType> getDeniedPermissions() {
        Collection<PermissionType> types = new HashSet<>();
        for (PermissionType type : PermissionType.values()) {
            if (getState(type) == PermissionState.DENIED) {
                types.add(type);
            }
        }
        return Collections.unmodifiableCollection(types);
    }

    /**
     * Gets a collection with permission types which are set to ({@link PermissionState#NONE}).
     *
     * @return A collection with all unset permissions.
     */
    default Collection<PermissionType> getUnsetPermissions() {
        Collection<PermissionType> types = new HashSet<>();
        for (PermissionType type : PermissionType.values()) {
            if (getState(type) == PermissionState.NONE) {
                types.add(type);
            }
        }
        return Collections.unmodifiableCollection(types);
    }

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
