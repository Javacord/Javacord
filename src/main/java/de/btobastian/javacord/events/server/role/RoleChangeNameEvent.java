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
package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A role change name.
 */
public class RoleChangeNameEvent extends RoleEvent {

    /**
     * The old name of the role.
     */
    private final String oldName;

    /**
     * Creates a new role change name.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param oldName The old name of the role.
     */
    public RoleChangeNameEvent(DiscordApi api, Server server, Role role, String oldName) {
        super(api, server, role);
        this.oldName = oldName;
    }

    /**
     * Gets the old name of the role.
     *
     * @return The old name of the role.
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Gets the new name of the role.
     *
     * @return The new name of the role.
     */
    public String getNewName() {
        // TODO: return getRole().getName();
        return null;
    }
}
