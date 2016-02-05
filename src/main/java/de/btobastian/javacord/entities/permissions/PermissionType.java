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
package de.btobastian.javacord.entities.permissions;

/**
 * This enum contains all types of permissions.
 */
public enum PermissionType {

    // general
    CREATE_INSTANT_INVITE(0),
    KICK_MEMBERS(1),
    BAN_MEMBERS(2),
    MANAGE_ROLES(3),
    MANAGE_PERMISSIONS(3),
    MANAGE_CHANNELS(4),
    MANAGE_CHANNEL(4),
    MANAGE_SERVER(5),

    // chat
    READ_MESSAGES(10),
    SEND_MESSAGES(11),
    SEND_TTS_MESSAGES(12),
    MANAGE_MESSAGES(13),
    EMBED_LINKS(14),
    ATTACH_FILE(15),
    READ_MESSAGE_HISTORY(16),
    MENTION_EVERYONE(17),

    // voice
    VOICE_CONNECT(20),
    VOICE_SPEAK(21),
    VOICE_MUTE_MEMBERS(22),
    VOICE_DEAFEN_MEMBERS(23),
    VOICE_MOVE_MEMBERS(24),
    VOICE_USE_VAD(25);

    private int offset;

    private PermissionType(int offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset of the permissions type.
     *
     * @return The offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Checks if the permission is "included" in the given integer.
     *
     * @param i The integer to check.
     * @return Whether the permission is "included" or not.
     */
    public boolean isSet(int i) {
        return (i & (1 << offset)) != 0;
    }

}
