/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.entities.permissions.impl;

import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.PermissionsBuilder;

/**
 * The implementation of the permissions builder interface.
 */
public class ImplPermissionsBuilder implements PermissionsBuilder {

    private int allowed = 0;
    private int denied = 0;

    /**
     * Creates a new instance of this class.
     */
    public ImplPermissionsBuilder() { }

    /**
     * Creates a new instance of this class.
     *
     * @param permissions The permissions which should be copied.
     */
    public ImplPermissionsBuilder(Permissions permissions) {
        allowed = ((ImplPermissions) permissions).getAllowed();
        denied = ((ImplPermissions) permissions).getDenied();
    }

    @Override
    public PermissionsBuilder setState(PermissionType type, PermissionState state) {
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
        return this;
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
