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
package de.btobastian.javacord.entities.message.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.exceptions.PermissionsException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The implementation of the user interface.
 */
public class ImplMessage implements Message {

    private ImplDiscordAPI api;

    private String id;
    private String content = null;
    private boolean tts;
    private User author;
    private final List<User> mentions = new ArrayList<>();
    private MessageReceiver receiver;
    private String channelId;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param api The api of this server.
     */
    public ImplMessage(JSONObject data, ImplDiscordAPI api, MessageReceiver receiver) {
        this.api = api;
        this.receiver = receiver;

        id = data.getString("id");
        if (data.has("content")) {
            content = data.getString("content");
        }
        tts = data.getBoolean("tts");
        author = api.getUserById(data.getJSONObject("author").getString("id"));

        JSONArray mentions = data.getJSONArray("mentions");
        for (int i = 0; i < mentions.length(); i++) {
            String userId = mentions.getJSONObject(i).getString("id");
            User user = api.getUserById(userId);
            this.mentions.add(user);
        }

        channelId = data.getString("channel_id");
        if (receiver == null) {
            outer: for (Server server : api.getServers()) {
                for (Channel c : server.getChannels()) {
                    if (c.getId().equals(channelId)) {
                        this.receiver = c;
                        break outer;
                    }
                }
            }
            for (User user : api.getUsers()) {
                if (channelId.equals(((ImplUser) user).getUserChannelId())) {
                    if (user != author) {
                        this.receiver = user;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Channel getChannelReceiver() {
        if (receiver instanceof Channel) {
            return (Channel) receiver;
        }
        return null;
    }

    @Override
    public User getUserReceiver() {
        if (receiver instanceof User) {
            return (User) receiver;
        }
        return null;
    }

    @Override
    public MessageReceiver getReceiver() {
        return receiver;
    }

    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public boolean isPrivateMessage() {
        return getUserReceiver() != null;
    }

    @Override
    public List<User> getMentions() {
        return new ArrayList<>(mentions);
    }

    @Override
    public boolean isTts() {
        return tts;
    }

    @Override
    public Future<Exception> delete() {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    HttpResponse<JsonNode> response = Unirest.delete
                            ("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId())
                            .header("authorization", api.getToken())
                            .asJson();
                    if (response.getStatus() == 403) {
                        throw new PermissionsException("Missing permissions!");
                    }
                    if (response.getStatus() > 199 && response.getStatus() < 300) {
                        throw new Exception("Received http status code " + response.getStatus()
                                + " with message " + response.getStatusText());
                    }
                    return null;
                } catch (Exception e) {
                    return e;
                }
            }
        });
    }
}
