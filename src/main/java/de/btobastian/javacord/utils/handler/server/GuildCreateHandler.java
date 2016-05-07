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
package de.btobastian.javacord.utils.handler.server;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.listener.server.ServerJoinListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

/**
 * Handles the guild create packet.
 */
public class GuildCreateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GuildCreateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildCreateHandler(ImplDiscordAPI api) {
        super(api, true, "GUILD_CREATE");
    }

    @Override
    public void handle(JSONObject packet) {
        if (packet.has("unavailable") && packet.getBoolean("unavailable")) {
            return;
        }
        String id = packet.getString("id");
        if (api.getUnavailableServers().contains(id)) {
            api.getUnavailableServers().remove(id);
            new ImplServer(packet, api);
            return;
        }
        if (api.getServerById(id) != null) {
            // TODO update information
            return;
        }
        final Server server = new ImplServer(packet, api);
        listenerExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<ServerJoinListener> listeners = api.getListeners(ServerJoinListener.class);
                synchronized (listeners) {
                    for (ServerJoinListener listener : listeners) {
                        try {
                            listener.onServerJoin(api, server);
                        } catch (Throwable t) {
                            logger.warn("Uncaught exception in ServerJoinListener!", t);
                        }
                    }
                }
            }
        });
        api.getThreadPool().getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                // can cause a deadlock if someone blocks a listener
                // with the #createServer or #acceptInvite method + #get
                api.getInternalServerJoinListener().onServerJoin(api, server);
            }
        });
    }

}
