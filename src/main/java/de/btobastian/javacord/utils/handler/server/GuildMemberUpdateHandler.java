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
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.events.server.role.UserRoleAddEvent;
import de.btobastian.javacord.events.server.role.UserRoleRemoveEvent;
import de.btobastian.javacord.events.user.UserChangeNicknameEvent;
import de.btobastian.javacord.listeners.server.role.UserRoleAddListener;
import de.btobastian.javacord.listeners.server.role.UserRoleRemoveListener;
import de.btobastian.javacord.listeners.user.UserChangeNicknameListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Handles the guild member update packet.
 */
public class GuildMemberUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMemberUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_MEMBER_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        api.getServerById(packet.getString("guild_id")).map(server -> (ImplServer) server).ifPresent(server -> {
            User user = api.getOrCreateUser(packet.getJSONObject("user"));
            if (packet.has("nick")) {
                String newNickname = packet.optString("nick", null);
                String oldNickname = server.getNickname(user).orElse(null);
                if (!Objects.deepEquals(newNickname, oldNickname)) {
                    server.setNickname(user, newNickname);

                    UserChangeNicknameEvent event =
                            new UserChangeNicknameEvent(api, user, server, newNickname, oldNickname);

                    List<UserChangeNicknameListener> listeners = new ArrayList<>();
                    listeners.addAll(user.getUserChangeNicknameListeners());
                    listeners.addAll(server.getUserChangeNicknameListeners());
                    listeners.addAll(api.getUserChangeNicknameListeners());

                    dispatchEvent(listeners, listener -> listener.onUserChangeNickname(event));
                }
            }

            if (packet.has("roles")) {
                JSONArray jsonRoles = packet.getJSONArray("roles");
                Collection<Role> newRoles = new HashSet<>();
                Collection<Role> oldRoles = server.getRolesOf(user);
                Collection<Role> intersection = new HashSet<>();
                for (int i = 0; i < jsonRoles.length(); i++) {
                    api.getRoleById(jsonRoles.getString(i))
                            .map(role -> {
                                newRoles.add(role);
                                return role;
                            })
                            .filter(oldRoles::contains)
                            .ifPresent(intersection::add);
                }

                // Added roles
                Collection<Role> addedRoles = new ArrayList<>(newRoles);
                addedRoles.removeAll(intersection);
                for (Role role : addedRoles) {
                    if (role.isEveryoneRole()) {
                        continue;
                    }
                    ((ImplRole) role).addUserToCache(user);
                    UserRoleAddEvent event = new UserRoleAddEvent(api, role, user);

                    List<UserRoleAddListener> listeners = new ArrayList<>();
                    listeners.addAll(user.getUserRoleAddListeners());
                    listeners.addAll(role.getUserRoleAddListeners());
                    listeners.addAll(role.getServer().getUserRoleAddListeners());
                    listeners.addAll(api.getUserRoleAddListeners());

                    dispatchEvent(listeners, listener -> listener.onUserRoleAdd(event));
                }

                // Removed roles
                Collection<Role> removedRoles = new ArrayList<>(oldRoles);
                removedRoles.removeAll(intersection);
                for (Role role : removedRoles) {
                    if (role.isEveryoneRole()) {
                        continue;
                    }
                    ((ImplRole) role).removeUserFromCache(user);
                    UserRoleRemoveEvent event = new UserRoleRemoveEvent(api, role, user);

                    List<UserRoleRemoveListener> listeners = new ArrayList<>();
                    listeners.addAll(user.getUserRoleRemoveListeners());
                    listeners.addAll(role.getUserRoleRemoveListeners());
                    listeners.addAll(role.getServer().getUserRoleRemoveListeners());
                    listeners.addAll(api.getUserRoleRemoveListeners());

                    dispatchEvent(listeners, listener -> listener.onUserRoleRemove(event));
                }
            }
        });
    }

}