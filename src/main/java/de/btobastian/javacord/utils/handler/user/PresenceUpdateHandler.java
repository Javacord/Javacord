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
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.user.UserChangeGameListener;
import de.btobastian.javacord.listener.user.UserChangeNameListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.List;

/**
 * This class handles the presence update packet.
 */
public class PresenceUpdateHandler extends PacketHandler {

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
        User user = api.getOrCreateUser(packet.getJSONObject("user"));

        // check username
        if (packet.getJSONObject("user").has("username")) {
            String name = packet.getJSONObject("user").getString("username");
            if (!user.getName().equals(name)) {
                String oldName = user.getName();
                ((ImplUser) user).setName(name);
                List<Listener> listeners = api.getListeners(UserChangeNameListener.class);
                synchronized (listeners) {
                    for (Listener listener : listeners) {
                        ((UserChangeNameListener) listener).onUserChangeName(api, user, oldName);
                    }
                }
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
                    List<Listener> listeners = api.getListeners(UserChangeGameListener.class);
                    synchronized (listeners) {
                        for (Listener listener : listeners) {
                            ((UserChangeGameListener) listener).onUserChangeGame(api, user, oldGame);
                        }
                    }
                }
            }
        }
    }

}
