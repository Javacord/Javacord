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
 * A Role Change Managed event.
 */
public class RoleChangeManagedEvent extends RoleEvent {

    /**
     * The old managed value.
     */
    private final boolean oldManaged;

    /**
     * Creates a new RoleChangeManagedEvent
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param oldManaged The old managed flag of the role.
     */
    public RoleChangeManagedEvent(DiscordApi api, Server server, Role role, boolean oldManaged) {
        super(api, server, role);
        this.oldManaged = oldManaged;
    }

    /**
     * Gets the old Managed flag of the role.
     *
     * @return The old Managed flag of the role.
     */
    public boolean getOldManagedFlag() {
        return oldManaged;
    }

    /**
     * Gets the new Managed flag of the role.
     *
     * @return The new Managed flag of the role.
     */
    public boolean getNewManagedFlag() {
        return !oldManaged;
    }

}
