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
package de.btobastian.javacord.utils.handler.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.GameType;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.impl.ImplGame;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.events.user.UserChangeGameEvent;
import de.btobastian.javacord.events.user.UserChangeStatusEvent;
import de.btobastian.javacord.listeners.user.UserChangeGameListener;
import de.btobastian.javacord.listeners.user.UserChangeStatusListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the presence update packet.
 */
public class PresenceUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public PresenceUpdateHandler(DiscordApi api) {
        super(api, true, "PRESENCE_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        long userId = Long.parseLong(packet.getJSONObject("user").getString("id"));
        api.getUserById(userId).map(user -> ((ImplUser) user)).ifPresent(user -> {
            if (packet.has("game")) {
                Game newGame = null;
                if (!packet.isNull("game")) {
                    int gameType = packet.getJSONObject("game").getInt("type");
                    String name = packet.getJSONObject("game").getString("name");
                    String streamingUrl =
                            packet.getJSONObject("game").has("url") && !packet.getJSONObject("game").isNull("url") ?
                            packet.getJSONObject("game").getString("url") : null;
                    newGame = new ImplGame(GameType.getGameTypeById(gameType), name, streamingUrl);
                }
                Game oldGame = user.getGame().orElse(null);
                user.setGame(newGame);
                if (!Objects.deepEquals(newGame, oldGame)) {
                    dispatchUserGameChangeEvent(user, newGame, oldGame);
                }
            }
            if (packet.has("status")) {
                UserStatus newStatus = UserStatus.fromString(packet.optString("status"));
                UserStatus oldStatus = user.getStatus();
                user.setStatus(newStatus);
                if (newStatus != oldStatus) {
                    dispatchUserStatusChangeEvent(user, newStatus, oldStatus);
                }
            }
        });
    }

    private void dispatchUserGameChangeEvent(User user, Game newGame, Game oldGame) {
        UserChangeGameEvent event = new UserChangeGameEvent(api, user, newGame, oldGame);

        List<UserChangeGameListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeGameListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeGameListeners()));
        listeners.addAll(api.getUserChangeGameListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeGame(event));
    }

    private void dispatchUserStatusChangeEvent(User user, UserStatus newStatus, UserStatus oldStatus) {
        UserChangeStatusEvent event = new UserChangeStatusEvent(api, user, newStatus, oldStatus);

        List<UserChangeStatusListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeStatusListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeStatusListeners()));
        listeners.addAll(api.getUserChangeStatusListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeStatus(event));
    }

}