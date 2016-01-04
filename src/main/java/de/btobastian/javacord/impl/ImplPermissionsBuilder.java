package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.List;

import de.btobastian.javacord.permissions.PermissionState;
import de.btobastian.javacord.permissions.PermissionType;
import de.btobastian.javacord.permissions.Permissions;
import de.btobastian.javacord.permissions.PermissionsBuilder;

/**
 * The implementation of {@link PermissionsBuilder}.
 */
class ImplPermissionsBuilder implements PermissionsBuilder {

    private List<PermissionType> allowed = new ArrayList<>();
    private List<PermissionType> denied = new ArrayList<>();
    
    protected ImplPermissionsBuilder() {
        
    }
    
    protected ImplPermissionsBuilder(Permissions permissions) {
        for (PermissionType type : PermissionType.values()) {
            setState(type, permissions.getState(type));
        }
    }
    
    /*
     * (non-Javadoc)
     * @see de.btobastian.javacord.permissions.PermissionsBuilder#setState(de.btobastian.javacord.permissions.PermissionType, de.btobastian.javacord.permissions.PermissionState)
     */
    @Override
    public void setState(PermissionType type, PermissionState state) {
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

    @Override
    public Permissions build() {
        return new ImplPermissions(getAllow(), getDeny());
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

}
