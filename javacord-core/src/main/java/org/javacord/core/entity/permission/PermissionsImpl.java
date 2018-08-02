package org.javacord.core.entity.permission;

import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;

/**
 * The implementation of the permissions interface.
 */
public class PermissionsImpl implements Permissions {

    /**
     * A permissions object with all permission types being {@link PermissionState#UNSET}.
     */
    public static final Permissions EMPTY_PERMISSIONS = new PermissionsImpl(0, 0);

    private final int allowed;
    private final int denied;

    /**
     * Creates a new instance of this class.
     *
     * @param allow An int containing all allowed permission types.
     * @param deny An int containing all denied permission types.
     */
    public PermissionsImpl(int allow, int deny) {
        this.allowed = allow;
        this.denied = deny;
    }

    /**
     * Creates a new instance of this class.
     *
     * @param allow An int containing all allowed permission types.
     *              Every other type will be set to denied.
     */
    public PermissionsImpl(int allow) {
        this.allowed = allow;
        int tempDenied = 0;
        for (PermissionType type : PermissionType.values()) {
            if (!type.isSet(allow)) {
                // set everything which is not allowed to denied.
                tempDenied = type.set(tempDenied, true);
            }
        }
        this.denied = tempDenied;
    }

    @Override
    public int getAllowedBitmask() {
        return allowed;
    }

    @Override
    public int getDeniedBitmask() {
        return denied;
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
    public boolean isEmpty() {
        return allowed == 0 && denied == 0;
    }

    @Override
    public int hashCode() {
        int hash = 42;

        hash = hash * 11 + allowed;
        hash = hash * 17 + denied;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PermissionsImpl)) {
            return false;
        }
        PermissionsImpl other = (PermissionsImpl) obj;
        return other.allowed == allowed && other.denied == denied;
    }

    @Override
    public String toString() {
        return "Permissions (allowed: " + getAllowedBitmask() + ", denied: " + getDeniedBitmask() + ")";
    }

}
