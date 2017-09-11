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
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A Role event.
 */
public abstract class RoleEvent extends ServerEvent {

    /**
     * The role of the event
     */
    private final Role role;

    /**
     * Creates a new role event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     */
    public RoleEvent(DiscordApi api, Server server, Role role) {
        super(api, server);
        this.role = role;
    }

    /**
     * Gets the Role of the event.
     *
     * @return The role of the event.
     */
    public Role getRole() {
        return role;
    }
}
