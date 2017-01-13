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
package de.btobastian.javacord.entities.permissions.impl;


import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.permissions.Ban;
import org.json.JSONObject;

/**
 * The implementation of the ban interface.
 */
public class ImplBan implements Ban {

    private final ImplServer server;
    private final User user;
    private final String reason;

    public ImplBan(ImplDiscordAPI api, ImplServer server, JSONObject data) {
        this.server = server;
        this.user = api.getOrCreateUser(data.getJSONObject("user"));
        this.reason = data.isNull("reason") ? null : data.getString("reason");
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String getReason() {
        return reason;
    }
}