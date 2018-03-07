package org.javacord.entity.permission.impl;

import org.javacord.entity.permission.PermissionState;
import org.javacord.entity.permission.PermissionType;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsFactory;

/**
 * The implementation of {@link PermissionsFactory}.
 */
public class ImplPermissionsFactory implements PermissionsFactory {

    /**
     * The integer containing all allowed permission types
     */
    private int allowed = 0;

    /**
     * The integer containing all denied permission types.
     */
    private int denied = 0;

    /**
     * Creates a new permissions factory.
     */
    public ImplPermissionsFactory() { }

    /**
     * Creates a new permissions factory with the states of the given permissions object.
     *
     * @param permissions The permissions which should be copied.
     */
    public ImplPermissionsFactory(Permissions permissions) {
        allowed = permissions.getAllowedBitmask();
        denied = permissions.getDeniedBitmask();
    }

    @Override
    public void setState(PermissionType type, PermissionState state) {
        switch (state) {
            case ALLOWED:
                allowed = type.set(allowed, true);
                denied = type.set(denied, false);
                break;
            case DENIED:
                allowed = type.set(allowed, false);
                denied = type.set(denied, true);
                break;
            case NONE:
                allowed = type.set(allowed, false);
                denied = type.set(denied, false);
                break;
        }
    }

    @Override
    public PermissionState getState(PermissionType type) {
        if (type.isSet(allowed)) {
            return PermissionState.ALLOWED;
        }
        if (type.isSet(denied)) {
            return PermissionState.DENIED;
        }
        return PermissionState.NONE;
    }

    @Override
    public Permissions build() {
        return new ImplPermissions(allowed, denied);
    }

}
