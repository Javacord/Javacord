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
import de.btobastian.javacord.events.Event;

/**
 * A server event.
 */
public abstract class ServerEvent extends Event {

    /**
     * The server of the event.
     */
    private final Server server;

    /**
     * Creates a new server event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     */
    public ServerEvent(DiscordApi api, Server server) {
        super(api);
        this.server = server;
    }

    /**
     * Gets the server of the event.
     *
     * @return The server of the event.
     */
    public Server getServer() {
        return server;
    }

}
