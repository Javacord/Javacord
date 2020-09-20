package org.javacord.api.entity.permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * A collection of all existing {@link PermissionType permission types} and their states.
 */
public interface Permissions {

    /**
     * Gets the long containing all allowed permission types.
     *
     * @return The long containing all allowed permission types.
     */
    long getAllowedBitmask();

    /**
     * Gets the long containing all denied permission types.
     *
     * @return The long containing all denied permission types.
     */
    long getDeniedBitmask();

    /**
     * Gets the state of the given type.
     *
     * @param type The type.
     * @return The state of the type.
     */
    PermissionState getState(PermissionType type);

    /**
     * Creates a new permissions builder from this permissions object.
     *
     * @return The created builder.
     * @see PermissionsBuilder#PermissionsBuilder(Permissions)
     */
    default PermissionsBuilder toBuilder() {
        return new PermissionsBuilder(this);
    }

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
     * Gets a collection with permission types which are set to ({@link PermissionState#UNSET}).
     *
     * @return A collection with all unset permissions.
     */
    default Collection<PermissionType> getUnsetPermissions() {
        return Collections.unmodifiableCollection(Arrays.stream(PermissionType.values())
                .filter(type -> getState(type) == PermissionState.UNSET)
                .collect(Collectors.toSet()));
    }

    /**
     * Checks if the all permission types are set to {@link PermissionState#UNSET}.
     *
     * @return Whether all permission types are set to UNSET or not.
     */
    boolean isEmpty(); // We could check it in a default method, but it's faster to just check it in the implementation

    /**
     * Creates a {@code Permissions} object from the given bitmask.
     * Permissions that are not included are marked as {@link PermissionState#UNSET}.
     *
     * @param bitmask The bitmask of allowed permissions.
     * @return A {@code Permissions} object created from the given bitmask.
     */
    static Permissions fromBitmask(int bitmask) {
        return fromBitmask(bitmask, 0);
    }

    /**
     * Creates a {@code Permissions} object from the given bitmasks.
     *
     * <p>Permissions that are not included in any bitmap are marked as {@link PermissionState#UNSET}.
     * If a permission is included in both the allowed and denied bitmap,
     * it is marked as {@link PermissionState#UNSET}.
     *
     * @param allowedBitmask The bitmask of allowed permissions.
     * @param deniedBitmask  The bitmask of denied permissions.
     * @return A {@code Permissions} object created from the given bitmasks.
     */
    static Permissions fromBitmask(int allowedBitmask, int deniedBitmask) {
        PermissionsBuilder permissionsBuilder = new PermissionsBuilder();

        for (PermissionType permissionType : PermissionType.values()) {
            if (permissionType.isSet(allowedBitmask) && permissionType.isSet(deniedBitmask)) {
                permissionsBuilder.setState(permissionType, PermissionState.UNSET);
            } else if (permissionType.isSet(allowedBitmask)) {
                permissionsBuilder.setState(permissionType, PermissionState.ALLOWED);
            } else if (permissionType.isSet(deniedBitmask)) {
                permissionsBuilder.setState(permissionType, PermissionState.DENIED);
            } else {
                permissionsBuilder.setState(permissionType, PermissionState.UNSET);
            }
        }

        return permissionsBuilder.build();
    }

}
