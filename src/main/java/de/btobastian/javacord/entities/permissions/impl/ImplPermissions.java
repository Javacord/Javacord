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

/**
 * The implementation of the permissions interface.
 */
public class ImplPermissions implements Permissions {

    private final int allowed;
    private int denied;

    /**
     * Creates a new instance of this class.
     *
     * @param allow An int containing all allowed permission types.
     * @param deny An int containing all denied permission types.
     */
    public ImplPermissions(int allow, int deny) {
        this.allowed = allow;
        this.denied = deny;
    }

    /**
     * Creates a new instance of this class.
     *
     * @param allow An int containing all allowed permission types.
     *              Every other type will be set to denied.
     */
    public ImplPermissions(int allow) {
        this.allowed = allow;
        for (PermissionType type : PermissionType.values()) {
            if (!type.isSet(allow)) {
                // set everything which is not allowed to denied.
                denied = type.set(denied, true);
            }
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

    /**
     * Gets the integer containing all allowed permission types.
     *
     * @return The integer containing all allowed permission types.
     */
    public int getAllowed() {
        return allowed;
    }

    /**
     * Gets the integer containing all denied permission types.
     *
     * @return The integer containing all denied permission types.
     */
    public int getDenied() {
        return denied;
    }

    @Override
    public String toString() {
        return "Permissions (allowed: " + getAllowed() + ", denied: " + getDenied() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImplPermissions)) {
            return false;
        }
        ImplPermissions other = (ImplPermissions) obj;
        return other.allowed == allowed && other.denied == denied;
    }

}
