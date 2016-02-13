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
package de.btobastian.javacord.entities.permissions.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.exceptions.PermissionsException;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.role.RoleChangeNameListener;
import de.btobastian.javacord.listener.role.RoleChangePermissionsListener;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * The implementation of the role interface.
 */
public class ImplRole implements Role {

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);

    // key = channelId
    private final ConcurrentHashMap<String, Permissions> overwrittenPermissions = new ConcurrentHashMap<>();

    private final ImplDiscordAPI api;

    private final String id;
    private String name;
    private final ImplServer server;
    private ImplPermissions permissions;
    private int position;
    private Color color;
    private boolean hoist;

    private final List<User> users = new ArrayList<>();

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param server The server of the role.
     * @param api The api of this server.
     */
    public ImplRole(JSONObject data, ImplServer server, ImplDiscordAPI api) {
        this.server = server;
        this.api = api;

        id = data.getString("id");
        name = data.getString("name");
        permissions = new ImplPermissions(data.getInt("permissions"));
        position = data.getInt("position");
        color = new Color(data.getInt("color"));
        hoist = data.getBoolean("hoist");

        server.addRole(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Permissions getPermissions() {
        return permissions;
    }

    @Override
    public Permissions getOverwrittenPermissions(Channel channel) {
        Permissions overwrittenPermissions = this.overwrittenPermissions.get(channel.getId());
        if (overwrittenPermissions == null) {
            overwrittenPermissions = emptyPermissions;
        }
        return overwrittenPermissions;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean getHoist() {
        return hoist;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Future<Exception> updatePermissions(Permissions permissions) {
        return update(name, color.getRGB(), hoist, ((ImplPermissions) permissions).getAllowed());
    }

    @Override
    public Future<Exception> updateName(String name) {
        return update(name, color.getRGB(), hoist, permissions.getAllowed());
    }

    /**
     * Updates the role.
     *
     * @param name The new name of the role.
     * @param color The new color of the role.
     * @param hoist The new hoist of the role.
     * @param allow The new permissions of the role.
     * @return A future.
     */
    private Future<Exception> update(final String name, final int color, final boolean hoist, final int allow) {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    HttpResponse<JsonNode> response = Unirest.put
                            ("https://discordapp.com/api/guilds/" + server.getId() + "/roles/" + id)
                            .header("authorization", api.getToken())
                            .body(new JSONObject()
                                    .put("name", name)
                                    .put("color", color)
                                    .put("hoist", hoist)
                                    .put("permissions", allow))
                            .asJson();
                    if (response.getStatus() == 403) {
                        throw new PermissionsException("Missing permissions!");
                    }
                    if (response.getStatus() < 200 || response.getStatus() > 299) {
                        throw new Exception("Received http status code " + response.getStatus()
                                + " with message " + response.getStatusText());
                    }

                    // update permissions
                    if (ImplRole.this.permissions.getAllowed() != allow) {
                        final ImplPermissions oldPermissions = ImplRole.this.permissions;
                        ImplRole.this.permissions = new ImplPermissions(allow);
                        // call listener
                        api.getThreadPool().getExecutorService().submit(new Runnable() {
                            @Override
                            public void run() {
                                List<Listener> listeners =  api.getListeners(RoleChangePermissionsListener.class);
                                synchronized (listeners) {
                                    for (Listener listener : listeners) {
                                        ((RoleChangePermissionsListener) listener)
                                                .onRoleChangePermissions(api, ImplRole.this, oldPermissions);
                                    }
                                }
                            }
                        });
                    }

                    // update name
                    if (ImplRole.this.name != name) {
                        final String oldName = ImplRole.this.name;
                        ImplRole.this.name = name;
                        // call listener
                        api.getThreadPool().getExecutorService().submit(new Runnable() {
                            @Override
                            public void run() {
                                List<Listener> listeners =  api.getListeners(RoleChangeNameListener.class);
                                synchronized (listeners) {
                                    for (Listener listener : listeners) {
                                        ((RoleChangeNameListener) listener)
                                                .onRoleChangeName(api, ImplRole.this, oldName);
                                    }
                                }
                            }
                        });
                    }

                    // TODO no listeners for color and hoist atm
                    ImplRole.this.color = new Color(color);
                    ImplRole.this.hoist = hoist;
                    return null;
                } catch (Exception e) {
                    return e;
                }
            }
        });
    }

    /**
     * Adds an user.
     *
     * @param user The user to add.
     */
    public void addUser(User user) {
        synchronized (users) {
            users.add(user);
        }
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the permissions of the channel.
     *
     * @param permissions The permissions to set.
     */
    public void setPermissions(ImplPermissions permissions) {
        this.permissions = permissions;
    }

    /**
     * Sets the position of the channel.
     *
     * @param position The position to set.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets overwritten permissions.
     *
     * @param channel The channel which overwrites the permissions.
     * @param permissions The overwritten permissions to set.
     */
    public void setOverwrittenPermissions(Channel channel, Permissions permissions) {
        overwrittenPermissions.put(channel.getId(), permissions);
    }

}
