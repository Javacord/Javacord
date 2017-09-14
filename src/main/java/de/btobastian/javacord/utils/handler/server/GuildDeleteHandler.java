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
import de.btobastian.javacord.events.server.ServerBecomesUnavailableEvent;
import de.btobastian.javacord.events.server.ServerLeaveEvent;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the guild delete packet.
 */
public class GuildDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildDeleteHandler(DiscordApi api) {
        super(api, true, "GUILD_DELETE");
    }

    @Override
    public void handle(JSONObject packet) {
        long serverId = Long.parseLong(packet.getString("id"));
        if (packet.has("unavailable") && packet.getBoolean("unavailable")) {
            api.addUnavailableServerToCache(serverId);
            api.getServerById(serverId).ifPresent(server -> {
                ServerBecomesUnavailableEvent event = new ServerBecomesUnavailableEvent(api, server);

                List<ServerBecomesUnavailableListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerBecomesUnavailableListeners());
                listeners.addAll(api.getServerBecomesUnavailableListeners());

                dispatchEvent(listeners, listener -> listener.onServerBecomesUnavailable(event));
            });
            api.removeServerFromCache(serverId);
            return;
        }
        api.getServerById(serverId).ifPresent(server -> {
            ServerLeaveEvent event = new ServerLeaveEvent(api, server);

            List<ServerLeaveListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerLeaveListeners());
            listeners.addAll(api.getServerLeaveListeners());

            dispatchEvent(listeners, listener -> listener.onServerLeave(event));
        });
        api.removeServerFromCache(serverId);
    }

}