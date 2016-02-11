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

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of the role interface.
 */
public class ImplRole implements Role {

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);

    // key = channelId
    private final ConcurrentHashMap<String, Permissions> overwrittenPermissions = new ConcurrentHashMap<>();

    private final String id;
    private String name;
    private final ImplServer server;
    private Permissions permissions;
    private int position;
    private Color color;
    private boolean hoist;

    private final List<User> users = new ArrayList<>();

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param server The server of the role.
     */
    public ImplRole(JSONObject data, ImplServer server) {
        this.server = server;

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
    public void setPermissions(Permissions permissions) {
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
