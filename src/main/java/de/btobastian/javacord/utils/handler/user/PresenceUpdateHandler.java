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
package de.btobastian.javacord.utils.handler.user;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.listener.user.UserChangeGameListener;
import de.btobastian.javacord.listener.user.UserChangeNameListener;
import de.btobastian.javacord.listener.user.UserChangeStatusListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

/**
 * This class handles the presence update packet.
 */
public class PresenceUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(PresenceUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public PresenceUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "PRESENCE_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        final User user = api.getOrCreateUser(packet.getJSONObject("user"));

        Server server = null;
        if (packet.has("guild_id")) {
            server = api.getServerById(packet.getString("guild_id"));
        }
        if (server != null) {
            // add user to server
            ((ImplServer) server).addMember(user);
        }
        if (server != null && packet.has("roles")) {
            JSONArray roleIds = packet.getJSONArray("roles");
            for (int i = 0; i < roleIds.length(); i++) {
                // add user to the role
                ((ImplRole) server.getRoleById(roleIds.getString(i))).addUserNoUpdate(user);
            }
        }

        // check status
        if (packet.has("status")) {
            UserStatus status = UserStatus.fromString(packet.getString("status"));
            final UserStatus oldStatus = user.getStatus();
            ((ImplUser) user).setStatus(status);
            listenerExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<UserChangeStatusListener> listeners = api.getListeners(UserChangeStatusListener.class);
                    synchronized (listeners) {
                        for (UserChangeStatusListener listener : listeners) {
                            try {
                                listener.onUserChangeStatus(api, user, oldStatus);
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in UserChangeStatusListener!", t);
                            }
                        }
                    }
                }
            });
        }

        // check username
        if (packet.getJSONObject("user").has("username")) {
            String name = packet.getJSONObject("user").getString("username");
            if (!user.getName().equals(name)) {
                final String oldName = user.getName();
                ((ImplUser) user).setName(name);
                listenerExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        List<UserChangeNameListener> listeners = api.getListeners(UserChangeNameListener.class);
                        synchronized (listeners) {
                            for (UserChangeNameListener listener : listeners) {
                                try {
                                    listener.onUserChangeName(api, user, oldName);
                                } catch (Throwable t) {
                                    logger.warn("Uncaught exception in UserChangeNameListener!", t);
                                }
                            }
                        }
                    }
                });
            }
        }

        // check game
        if (packet.has("game") && !packet.isNull("game")) {
            if (packet.getJSONObject("game").has("name")) {
                String game = packet.getJSONObject("game").get("name").toString();
                String oldGame = user.getGame();
                if ((game == null && oldGame != null)
                        || (game != null && oldGame == null)
                        || (game != null && !game.equals(oldGame))) {
                    ((ImplUser) user).setGame(game);
                    List<UserChangeGameListener> listeners = api.getListeners(UserChangeGameListener.class);
                    synchronized (listeners) {
                        for (UserChangeGameListener listener : listeners) {
                            try {
                                listener.onUserChangeGame(api, user, oldGame);
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in UserChangeGameListener!", t);
                            }
                        }
                    }
                }
            }
        }
    }

}
