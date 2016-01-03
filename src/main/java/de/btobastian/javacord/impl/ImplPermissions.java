package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.List;

import de.btobastian.javacord.permissions.PermissionState;
import de.btobastian.javacord.permissions.PermissionType;
import de.btobastian.javacord.permissions.Permissions;

class ImplPermissions implements Permissions {
    
    private List<PermissionType> allowed = new ArrayList<>();
    private List<PermissionType> denied = new ArrayList<>();
    
    protected ImplPermissions(int allow, int deny) {
        for (PermissionType type : PermissionType.values()) {
            if (isSet(allow, type)) {
                allowed.add(type);
            }
            if (isSet(deny, type)) {
                denied.add(type);
            }
        }
    }
    
    protected ImplPermissions(int allow) {
        for (PermissionType type : PermissionType.values()) {
            if (isSet(allow, type)) {
                allowed.add(type);
            } else {
                denied.add(type);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.api.permissions.Permission#getState(de.btobastian.javacord.api.permissions.PermissionType)
     */
    @Override
    public PermissionState getState(PermissionType type) {
        if (allowed.contains(type)) {
            return PermissionState.ALLOWED;
        }
        if (denied.contains(type)) {
            return PermissionState.DENIED;
        }
        return PermissionState.NONE;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImplPermissions)) {
            return false;
        }
        ImplPermissions other = (ImplPermissions) obj;
        for (PermissionType type : PermissionType.values()) {
            if (other.getState(type) != this.getState(type)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isSet(int i, PermissionType type) {
        return (i & (1 << type.getOffset())) != 0;
    }
    
}
