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
package de.btobastian.javacord.utils.handler.server.role;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.role.RoleDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.List;

/**
 * Handles the guild role delete packet.
 */
public class GuildRoleDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildRoleDeleteHandler(ImplDiscordAPI api) {
        super(api, true, "GUILD_ROLE_DELETE");
    }

    @Override
    public void handle(JSONObject packet) {
        String guildId = packet.getString("guild_id");
        String roleId = packet.getString("role_id");

        Server server = api.getServerById(guildId);
        Role role = server.getRoleById(roleId);

        if (role == null) {
            return;
        }

        ((ImplServer) server).removeRole(role);

        List<Listener> listeners =  api.getListeners(RoleDeleteListener.class);
        synchronized (listeners) {
            for (Listener listener : listeners) {
                ((RoleDeleteListener) listener).onRoleDelete(api, role);
            }
        }
    }

}
