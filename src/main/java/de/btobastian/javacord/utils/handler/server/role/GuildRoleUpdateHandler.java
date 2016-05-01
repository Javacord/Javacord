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
import de.btobastian.javacord.listener.role.*;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.awt.*;
import java.util.List;

/**
 * Handles the guild role update packet.
 */
public class GuildRoleUpdateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GuildRoleUpdateHandler.class);

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
        final ImplRole role = (ImplRole) server.getRoleById(roleJson.getString("id"));

        String name = roleJson.getString("name");
        if (!role.getName().equals(name)) {
            final String oldName = role.getName();
            role.setName(name);
            listenerExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<RoleChangeNameListener> listeners = api.getListeners(RoleChangeNameListener.class);
                    synchronized (listeners) {
                        for (RoleChangeNameListener listener : listeners) {
                            try {
                                listener.onRoleChangeName(api, role, oldName);
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in RoleChangeNameListener!", t);
                            }
                        }
                    }
                }
            });
        }

        Permissions permissions = new ImplPermissions(roleJson.getInt("permissions"));
        if (!role.getPermissions().equals(permissions)) {
            final Permissions oldPermissions = role.getPermissions();
            role.setPermissions((ImplPermissions) permissions);
            listenerExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<RoleChangePermissionsListener> listeners =
                            api.getListeners(RoleChangePermissionsListener.class);
                    synchronized (listeners) {
                        for (RoleChangePermissionsListener listener : listeners) {
                            try {
                                listener.onRoleChangePermissions(api, role, oldPermissions);
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in RoleChangePermissionsListener!", t);
                            }
                        }
                    }
                }
            });
        }

        Color color = new Color(roleJson.getInt("color"));
        if (role.getColor().getRGB() != color.getRGB()) {
            final Color oldColor = role.getColor();
            role.setColor(color);
            listenerExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<RoleChangeColorListener> listeners = api.getListeners(RoleChangeColorListener.class);
                    synchronized (listeners) {
                        for (RoleChangeColorListener listener : listeners) {
                            try {
                                listener.onRoleChangeColor(api, role, oldColor);
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in RoleChangeColorListener!", t);
                            }
                        }
                    }
                }
            });
        }

        if (role.getHoist() != roleJson.getBoolean("hoist")) {
            role.setHoist(!role.getHoist());
            listenerExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<RoleChangeHoistListener> listeners = api.getListeners(RoleChangeHoistListener.class);
                    synchronized (listeners) {
                        for (RoleChangeHoistListener listener : listeners) {
                            try {
                                listener.onRoleChangeHoist(api, role, !role.getHoist());
                            } catch (Throwable t) {
                                logger.warn("Uncaught exception in RoleChangeHoistListener!", t);
                            }
                        }
                    }
                }
            });
        }

        synchronized (Role.class) { // we don't want strange positions
            int position = roleJson.getInt("position");
            if (role.getPosition() != position) {
                final int oldPosition = role.getPosition();
                role.setPosition(position);
                listenerExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        List<RoleChangePositionListener> listeners = api.getListeners(RoleChangePositionListener.class);
                        synchronized (listeners) {
                            for (RoleChangePositionListener listener : listeners) {
                                try {
                                    listener.onRoleChangePosition(api, role, oldPosition);
                                } catch (Throwable t) {
                                    logger.warn("Uncaught exception in RoleChangePositionListener!", t);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

}
