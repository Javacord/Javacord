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
package de.btobastian.javacord.utils.handler.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerMemberAddEvent;
import de.btobastian.javacord.listeners.server.ServerMemberAddListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the guild member add packet.
 */
public class GuildMemberAddHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMemberAddHandler(DiscordApi api) {
        super(api, true, "GUILD_MEMBER_ADD");
    }

    @Override
    public void handle(JSONObject packet) {
        api.getServerById(packet.getString("guild_id"))
                .map(server -> (ImplServer) server)
                .ifPresent(server -> {
                    server.addMember(packet);
                    User user = api.getOrCreateUser(packet.getJSONObject("user"));

                    ServerMemberAddEvent event = new ServerMemberAddEvent(api, server, user);

                    List<ServerMemberAddListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerMemberAddListeners());
                    listeners.addAll(user.getServerMemberAddListeners());
                    listeners.addAll(api.getServerMemberAddListeners());

                    dispatchEvent(listeners, listener -> listener.onServerMemberAdd(event));
                });
    }

}