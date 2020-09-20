package org.javacord.core.entity.permission;

import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;

import java.util.Objects;

/**
 * The implementation of the permissions interface.
 */
public class PermissionsImpl implements Permissions {

    /**
     * A permissions object with all permission types being {@link PermissionState#UNSET}.
     */
    public static final Permissions EMPTY_PERMISSIONS = new PermissionsImpl(0, 0);

    private final long allowed;
    private final long denied;

    /**
     * Creates a new instance of this class.
     *
     * @param allow A long containing all allowed permission types.
     * @param deny  A long containing all denied permission types.
     */
    public PermissionsImpl(long allow, long deny) {
        this.allowed = allow;
        this.denied = deny;
    }

    /**
     * Creates a new instance of this class.
     *
     * @param allow A long containing all allowed permission types.
     *              Every other type will be set to denied.
     */
    public PermissionsImpl(long allow) {
        this.allowed = allow;
        long tempDenied = 0;
        for (PermissionType type : PermissionType.values()) {
            if (!type.isSet(allow)) {
                // set everything which is not allowed to denied.
                tempDenied = type.set(tempDenied, true);
            }
        }
        this.denied = tempDenied;
    }

    @Override
    public long getAllowedBitmask() {
        return allowed;
    }

    @Override
    public long getDeniedBitmask() {
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
        return Objects.hash(allowed, denied);
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
