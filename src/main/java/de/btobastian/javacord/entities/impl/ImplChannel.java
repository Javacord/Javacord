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
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.InviteBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.exceptions.PermissionsException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * The implementation of the channel interface.
 */
public class ImplChannel implements Channel {

    private ImplDiscordAPI api;

    private String id;
    private String name;
    private String topic = null;
    private int position;
    private ImplServer server;

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
        } catch (JSONException e) { }
        position = data.getInt("position");
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
        return null;
    }

    @Override
    public void type() {

    }

    @Override
    public InviteBuilder getInviteBuilder() {
        return null;
    }

    @Override
    public Future<Message> sendMessage(String content) {
        return sendMessage(content, false);
    }

    @Override
    public Future<Message> sendMessage(String content, boolean tts) {
        final CompletableFuture<Message> future = new CompletableFuture<>();
        sendMessage(content, tts, new FutureCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                future.complete(message);
            }

            @Override
            public void onFailure(Throwable throwable) {
                future.complete(null);
            }
        });
        return future;
    }

    @Override
    public void sendMessage(String content, FutureCallback<Message> callback) {
        sendMessage(content, false, callback);
    }

    @Override
    public void sendMessage(final String content, final boolean tts, FutureCallback<Message> callback) {
        final MessageReceiver receiver = this;
        Futures.addCallback(api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
            @Override
            public Message call() throws Exception {
                HttpResponse<JsonNode> response =
                        Unirest.post("https://discordapp.com/api/channels/" + id + "/messages")
                                .header("authorization", api.getToken())
                                .field("content", content)
                                .field("tts", tts)
                                .field("mentions", new String[0])
                                .asJson();
                if (response.getStatus() == 403) {
                    throw new PermissionsException("Missing permissions!");
                }
                if (response.getStatus() != 200) {
                    throw new Exception("Received http status code " + response.getStatus());
                }
                return new ImplMessage(response.getBody().getObject(), api, receiver);
            }
        }), callback);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
