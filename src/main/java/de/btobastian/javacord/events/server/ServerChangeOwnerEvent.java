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
package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

/**
 * A server change owner event.
 */
public class ServerChangeOwnerEvent extends ServerEvent {

    /**
     * The old owner of the server.
     */
    private final User oldOwner;

    /**
     * Creates a new server change owner event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param oldOwner The old owner of the server.
     */
    public ServerChangeOwnerEvent(DiscordApi api, Server server, User oldOwner) {
        super(api, server);
        this.oldOwner = oldOwner;
    }

    /**
     * Gets the old owner of the server.
     *
     * @return The old owner of the server.
     */
    public User getOldOwner() {
        return oldOwner;
    }

    /**
     * Gets the new owner of the server.
     *
     * @return The new owner of the server.
     */
    public User getNewOwner() {
        // TODO return getServer().getOwner();
        return null;
    }

}
