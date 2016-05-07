/*
 * Copyright (C) 2016 Bastian Oppermann
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
package de.btobastian.javacord.entities;

/**
 * An enum which contains all statuses.
 */
public enum UserStatus {

    /**
     * The user is online.
     */
    ONLINE(),

    /**
     * The user is idle.
     */
    IDLE(),

    /**
     * Ths user is offline.
     */
    OFFLINE();

    /**
     * Gets the status from the given String.
     *
     * @param str The string, e.g. "online".
     *
     * @return The status.
     */
    public static UserStatus fromString(String str) {
        switch (str) {
            case "online":
                return ONLINE;
            case "idle":
                return IDLE;
            default:
                return OFFLINE;
        }
    }

}
