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
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listener.server.ServerMemberBanListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

/**
 * Handles the guild ban add packet.
 */
public class GuildBanAddHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GuildBanAddHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildBanAddHandler(ImplDiscordAPI api) {
        super(api, true, "GUILD_BAN_ADD");
    }

    @Override
    public void handle(JSONObject packet) {
        final Server server = api.getServerById(packet.getString("guild_id"));
        final User user = api.getOrCreateUser(packet.getJSONObject("user"));
        if (server != null) {
            listenerExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<ServerMemberBanListener> listeners = api.getListeners(ServerMemberBanListener.class);
                    synchronized (listeners) {
                        for (ServerMemberBanListener listener : listeners) {
                            try {
                                listener.onServerMemberBan(api, user, server);
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in ServerMemberBanListener!", t);
                            }
                        }
                    }
                }
            });
        }
    }

}
