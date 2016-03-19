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
package de.btobastian.javacord.utils.handler;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Region;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * This class handles the ready packet.
 */
public class ReadyReconnectHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ReadyReconnectHandler(ImplDiscordAPI api) {
        // (READY_RECONNECT is dummy value)
        super(api, false, "READY_RECONNECT");
    }

    @Override
    public void handle(JSONObject packet) {
        // TODO improve this whole thing. e.g. it doesn't handle role removes etc. atm.
        long heartbeatInterval = packet.getLong("heartbeat_interval");
        api.getSocketAdapter().startHeartbeat(heartbeatInterval);

        JSONArray guilds = packet.getJSONArray("guilds"); // guild = server
        for (int i = 0; i < guilds.length(); i++) {
            JSONObject guild = guilds.getJSONObject(i);
            if (guild.has("unavailable") && guild.getBoolean("unavailable")) {
                continue;
            }
            Server server = api.getServerById(guild.getString("id"));
            if (server == null) {
                new ImplServer(guild, api);
                continue;
            }
            ((ImplServer) server).setName(guild.getString("name"));
            ((ImplServer) server).setRegion(Region.getRegionByKey(guild.getString("region")));
            ((ImplServer) server).setMemberCount(guild.getInt("member_count"));

            JSONArray members = new JSONArray();
            if (guild.has("members")) {
                members = guild.getJSONArray("members");
            }
            for (int j = 0; j < members.length(); j++) {
                User member = api.getOrCreateUser(members.getJSONObject(j).getJSONObject("user"));
                ((ImplServer) server).addMember(member);

                JSONArray memberRoles = members.getJSONObject(j).getJSONArray("roles");
                for (int k = 0; k < memberRoles.length(); k++) {
                    Role role = server.getRoleById(memberRoles.getString(k));
                    if (role != null) {
                        ((ImplRole) role).addUserNoUpdate(member);
                    }
                }
            }

            JSONArray presences = new JSONArray();
            if (guild.has("presences")) {
                presences = guild.getJSONArray("presences");
            }
            for (int j = 0; j < presences.length(); j++) {
                JSONObject presence = presences.getJSONObject(j);
                User user;
                try {
                    user = api.getUserById(presence.getJSONObject("user").getString("id")).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    continue;
                }
                if (presence.has("game") && !presence.isNull("game")) {
                    if (presence.getJSONObject("game").has("name") && !presence.getJSONObject("game").isNull("name")) {
                        ((ImplUser) user).setGame(presence.getJSONObject("game").getString("name"));
                    }
                }
            }
        }

        JSONArray privateChannels = packet.getJSONArray("private_channels");
        for (int i = 0; i < privateChannels.length(); i++) {
            JSONObject privateChannel = privateChannels.getJSONObject(i);
            String id = privateChannel.getString("id");
            User user = api.getOrCreateUser(privateChannel.getJSONObject("recipient"));
            ((ImplUser) user).setUserChannelId(id);
        }
    }

}
