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
package de.btobastian.javacord.entities.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.InviteBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.exceptions.PermissionsException;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelDeleteListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * The implementation of the channel interface.
 */
public class ImplChannel implements Channel {

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);

    private final ImplDiscordAPI api;

    private final String id;
    private String name;
    private String topic = null;
    private int position;
    private final ImplServer server;

    private ConcurrentHashMap<String, Permissions> overwrittenPermissions = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param server The server of the channel.
     * @param api The api of this server.
     */
    public ImplChannel(JSONObject data, ImplServer server, ImplDiscordAPI api) {
        this.api = api;
        this.server = server;

        id = data.getString("id");
        name = data.getString("name");
        try {
            topic = data.getString("topic");
        } catch (JSONException ignored) { }
        position = data.getInt("position");

        JSONArray permissionOverwrites = data.getJSONArray("permission_overwrites");
        for (int i = 0; i < permissionOverwrites.length(); i++) {
            JSONObject permissionOverwrite = permissionOverwrites.getJSONObject(i);
            String id = permissionOverwrite.getString("id");
            int allow = permissionOverwrite.getInt("allow");
            int deny = permissionOverwrite.getInt("deny");
            String type = permissionOverwrite.getString("type");
            if (type.equals("role")) {
                Role role = server.getRoleById(id);
                if (role != null) {
                    ((ImplRole) role).setOverwrittenPermissions(this, new ImplPermissions(allow, deny));
                }
            }
            if (type.equals("member")) {
                User user = api.getUserById(id);
                if (user != null) {
                    overwrittenPermissions.put(user.getId(), new ImplPermissions(allow, deny));
                }
            }
        }

        server.addChannel(this);
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
    public String getTopic() {
        return topic;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Future<Exception> delete() {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .delete("https://discordapp.com/api/channels/:id" + id)
                            .header("authorization", api.getToken())
                            .asJson();
                    if (response.getStatus() == 403) {
                        throw new PermissionsException("Missing permissions!");
                    }
                    if (response.getStatus() < 200 || response.getStatus() > 299) {
                        throw new Exception("Received http status code " + response.getStatus()
                                + " with message " + response.getStatusText());
                    }
                    server.removeChannel(ImplChannel.this);
                    // call listener
                    api.getThreadPool().getSingleThreadExecutorService("handlers").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(ChannelDeleteListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((ChannelDeleteListener) listener).onChannelDelete(api, ImplChannel.this);
                                }
                            }
                        }
                    });
                    return null;
                } catch (Exception e) {
                    return e;
                }
            }
        });
    }

    @Override
    public void type() {
        try {
            Unirest.post("https://discordapp.com/api/channels/" + id + "/typing")
                    .header("authorization", api.getToken())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InviteBuilder getInviteBuilder() {
        return new ImplInviteBuilder(this, api);
    }

    @Override
    public Future<Message> sendMessage(String content) {
        return sendMessage(content, false);
    }

    @Override
    public Future<Message> sendMessage(String content, boolean tts) {
        return sendMessage(content, tts, null);
    }

    @Override
    public Future<Message> sendMessage(String content, FutureCallback<Message> callback) {
        return sendMessage(content, false, callback);
    }

    @Override
    public Future<Message> sendMessage(final String content, final boolean tts, FutureCallback<Message> callback) {
        final MessageReceiver receiver = this;
        ListenableFuture<Message> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        HttpResponse<JsonNode> response =
                                Unirest.post("https://discordapp.com/api/channels/" + id + "/messages")
                                        .header("authorization", api.getToken())
                                        .header("content-type", "application/json")
                                        .body(new JSONObject()
                                                .put("content", content)
                                                .put("tts", tts)
                                                .put("mentions", new String[0]).toString())
                                        .asJson();
                        if (response.getStatus() == 403) {
                            throw new PermissionsException("Missing permissions!");
                        }
                        if (response.getStatus() < 200 || response.getStatus() > 299) {
                            throw new Exception("Received http status code " + response.getStatus()
                                    + " with message " + response.getStatusText());
                        }
                        return new ImplMessage(response.getBody().getObject(), api, receiver);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Message> sendFile(final File file) {
        return sendFile(file, null);
    }

    @Override
    public Future<Message> sendFile(final File file, FutureCallback<Message> callback) {
        final MessageReceiver receiver = this;
        ListenableFuture<Message> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        HttpResponse<JsonNode> response =
                                Unirest.post("https://discordapp.com/api/channels/" + id + "/messages")
                                        .header("authorization", api.getToken())
                                        .field("file", file)
                                        .asJson();
                        if (response.getStatus() == 403) {
                            throw new PermissionsException("Missing permissions!");
                        }
                        if (response.getStatus() < 200 || response.getStatus() > 299) {
                            throw new Exception("Received http status code " + response.getStatus()
                                    + " with message " + response.getStatusText());
                        }
                        return new ImplMessage(response.getBody().getObject(), api, receiver);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Permissions getOverwrittenPermissions(User user) {
        Permissions permissions = overwrittenPermissions.get(user.getId());
        return permissions == null ? emptyPermissions : permissions;
    }

    @Override
    public Permissions getOverwrittenPermissions(Role role) {
        return role.getOverwrittenPermissions(this);
    }

    /**
     * Sets the name of the channel (no update!).
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the topic of the channel (no update!).
     *
     * @param topic The topic to set.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Sets the position of the channel (no update!).
     *
     * @param position The position to set.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets the overwritten permissions of an user.
     *
     * @param user The user which overwrites the permissions.
     * @param permissions The overwritten permissions.
     */
    public void setOverwrittenPermissions(User user, Permissions permissions) {
        overwrittenPermissions.put(user.getId(), permissions);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
