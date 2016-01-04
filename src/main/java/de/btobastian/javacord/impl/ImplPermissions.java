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
    
    protected void setState(PermissionType type, PermissionState state) {
        switch (state) {
            case ALLOWED:
                allowed.add(type);
                denied.remove(type);
                break;
            case DENIED:
                allowed.remove(type);
                denied.add(type);
                break;
            case NONE:
                allowed.remove(type);
                denied.remove(type);
            default:
                break;
        }
    }

    protected int getAllow() {
        int allow = 0;
        for (PermissionType type : allowed) {
            allow += Math.pow(2, type.getOffset());
        }
        return allow;
    }
    
    protected int getDeny() {
        int deny = 0;
        for (PermissionType type : denied) {
            deny += Math.pow(2, type.getOffset());
        }
        return deny;
    }
    
    protected boolean isSet(int i, PermissionType type) {
        return (i & (1 << type.getOffset())) != 0;
    }
    
}
