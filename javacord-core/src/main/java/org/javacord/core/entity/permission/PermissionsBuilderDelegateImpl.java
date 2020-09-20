package org.javacord.core.entity.permission;

import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.internal.PermissionsBuilderDelegate;

/**
 * The implementation of {@link PermissionsBuilderDelegate}.
 */
public class PermissionsBuilderDelegateImpl implements PermissionsBuilderDelegate {

    /**
     * The long containing all allowed permission types.
     */
    private long allowed = 0;

    /**
     * The long containing all denied permission types.
     */
    private long denied = 0;

    /**
     * Creates a new permissions factory.
     */
    public PermissionsBuilderDelegateImpl() { }

    /**
     * Creates a new permissions factory with the states of the given permissions object.
     *
     * @param permissions The permissions which should be copied.
     */
    public PermissionsBuilderDelegateImpl(Permissions permissions) {
        allowed = permissions.getAllowedBitmask();
        denied = permissions.getDeniedBitmask();
    }

    @Override
    public void setState(PermissionType type, PermissionState state) {
        allowed = type.set(allowed, state == PermissionState.ALLOWED);
        denied = type.set(denied, state == PermissionState.DENIED);
    }

    @Override
    public PermissionState getState(PermissionType type) {
        if (type.isSet(allowed)) {
            return PermissionState.ALLOWED;
        }
        if (type.isSet(denied)) {
            return PermissionState.DENIED;
        }
        return PermissionState.UNSET;
    }

    @Override
    public Permissions build() {
        return new PermissionsImpl(allowed, denied);
    }

}
