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
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.role.RoleChangeNameListener;
import de.btobastian.javacord.listener.role.RoleChangePermissionsListener;
import de.btobastian.javacord.listener.role.RoleChangePositionListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.List;

/**
 * Handles the guild role update packet.
 */
public class GuildRoleUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildRoleUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "GUILD_ROLE_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        String guildId = packet.getString("guild_id");
        JSONObject roleJson = packet.getJSONObject("role");

        Server server = api.getServerById(guildId);
        ImplRole role = (ImplRole) server.getRoleById(roleJson.getString("id"));

        String name = roleJson.getString("name");
        if (!role.getName().equals(name)) {
            String oldName = role.getName();
            role.setName(name);
            List<Listener> listeners =  api.getListeners(RoleChangeNameListener.class);
            synchronized (listeners) {
                for (Listener listener : listeners) {
                    ((RoleChangeNameListener) listener).onRoleChangeName(api, role, oldName);
                }
            }
        }

        Permissions permissions = new ImplPermissions(roleJson.getInt("permissions"));
        if (!role.getPermission().equals(permissions)) {
            Permissions oldPermissions = role.getPermission();
            role.setPermissions(permissions);
            List<Listener> listeners =  api.getListeners(RoleChangePermissionsListener.class);
            synchronized (listeners) {
                for (Listener listener : listeners) {
                    ((RoleChangePermissionsListener) listener).onRoleChangePermissions(api, role, oldPermissions);
                }
            }
        }

        synchronized (Role.class) { // we don't want strange positions
            int position = roleJson.getInt("position");
            if (role.getPosition() != position) {
                int oldPosition = role.getPosition();
                role.setPosition(position);
                List<Listener> listeners =  api.getListeners(RoleChangePositionListener.class);
                synchronized (listeners) {
                    for (Listener listener : listeners) {
                        ((RoleChangePositionListener) listener).onRoleChangePosition(api, role, oldPosition);
                    }
                }
            }
        }
    }

}
