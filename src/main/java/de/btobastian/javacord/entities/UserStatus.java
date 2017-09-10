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
package de.btobastian.javacord.entities;

/**
 * An enum which contains all user status.
 */
public enum UserStatus {

    /**
     * The user is online.
     */
    ONLINE("online"),

    /**
     * The user is idle.
     */
    IDLE("idle"),
    /**
     * The user is dnd.
     */
    DO_NOT_DISTURB("dnd"),
    /**
     * The user is invisible. You cannot tell if a user is offline or invisible!
     * This is only used to change your own status, but you will never see an other user marked as invisible.
     */
    INVISIBLE("invisible"),
    /**
     * Ths user is offline.
     */
    OFFLINE("offline");

    private final String statusString;

    /**
     * Creates a new status.
     *
     * @param statusString The string which is used by Discord to identify the status.
     */
    UserStatus(String statusString) {
        this.statusString = statusString;
    }

    /**
     * Gets the status string of the status.
     *
     * @return The status string of the status.
     */
    public String getStatusString() {
        return statusString;
    }

    /**
     * Gets the status from the given string.
     *
     * @param str The string, e.g. "online".
     *
     * @return The status or {@link UserStatus#OFFLINE} if unknown string.
     */
    public static UserStatus fromString(String str) {
        for (UserStatus status : values()) {
            if (status.statusString.equals(str)) {
                return status;
            }
        }
        return OFFLINE;
    }

}