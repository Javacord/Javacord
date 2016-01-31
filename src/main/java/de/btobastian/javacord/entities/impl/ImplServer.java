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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.exceptions.PermissionsException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * The implementation of the server interface.
 */
public class ImplServer implements Server {

    private ImplDiscordAPI api;

    private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, User> members = new ConcurrentHashMap<>();

    private String id;
    private String name;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param api The api of this server.
     */
    public ImplServer(JSONObject data, ImplDiscordAPI api) {
        this.api = api;

        name = data.getString("name");
        id = data.getString("id");

        JSONArray channels = data.getJSONArray("channels");
        for (int i = 0; i < channels.length(); i++) {
            Channel channel = new ImplChannel(channels.getJSONObject(i), this, api);
            this.channels.put(channel.getId(), channel);
        }

        JSONArray members = new JSONArray();
        if (data.has("members")) {
            members = data.getJSONArray("members");
        }
        for (int i = 0; i < members.length(); i++) {
            User member = api.getOrCreateUser(members.getJSONObject(i));
            this.members.put(member.getId(), member);
        }

        api.getServerMap().put(id, this);
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
    public Future<Exception> deleteOrLeave() {
        final CompletableFuture<Exception> future = new CompletableFuture<>();
        api.getThreadPool().getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse<JsonNode> response = Unirest.delete("https://discordapp.com/api/guilds/" + id)
                            .header("authorization", api.getToken())
                            .asJson();
                    if (response.getStatus() == 403) {
                        future.complete(new PermissionsException("Missing permissions!"));
                    }
                    if (response.getStatus() != 200) {
                        future.complete(new Exception("Received http status code " + response.getStatus()));
                    }
                    api.getServerMap().remove(id);
                    future.complete(null);
                } catch (UnirestException e) {
                    future.complete(e);
                }
            }
        });
        return future;
    }

    @Override
    public Channel getChannelById(String id) {
        return channels.get(id);
    }

    @Override
    public Collection<Channel> getChannels() {
        return channels.values();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
