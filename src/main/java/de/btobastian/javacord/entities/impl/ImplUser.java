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
import com.mashape.unirest.http.exceptions.UnirestException;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.exceptions.PermissionsException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * The implementation of the user interface.
 */
public class ImplUser implements User {

    private ImplDiscordAPI api;

    private String id;
    private String name;
    private String avatarId = null;
    private String userChannelId = null;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param api The api of this server.
     */
    public ImplUser(JSONObject data, ImplDiscordAPI api) {
        this.api = api;

        id = data.getString("id");
        name = data.getString("username");
        try {
            avatarId = data.getString("avatar");
        } catch (JSONException e) { }

        api.getUserMap().put(id, this);
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
    public void type() {
        if (userChannelId == null) {
            return;
        }
        try {
            Unirest.post("https://discordapp.com/api/channels/" + userChannelId + "/typing")
                    .header("authorization", api.getToken())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isYourself() {
        return false; // TODO
    }

    @Override
    public Future<byte[]> getAvatarAsBytearray() {
        final CompletableFuture<byte[]> future = new CompletableFuture<>();
        getAvatarAsBytearray(new FutureCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                future.complete(bytes);
            }

            @Override
            public void onFailure(Throwable throwable) {
                future.complete(new byte[0]);
            }
        });
        return future;
    }

    @Override
    public void getAvatarAsBytearray(FutureCallback<byte[]> callback) {
        Futures.addCallback(api.getThreadPool().getListeningExecutorService().submit(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                if (avatarId == null) {
                    return new byte[0];
                }
                URL url = new URL("https://discordapp.com/api/users/" + id + "/avatars/" + avatarId + ".jpg");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("User-Agent", "Javacord");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                return out.toByteArray();
            }
        }), callback);
    }

    @Override
    public Future<BufferedImage> getAvatar() {
        final CompletableFuture<BufferedImage> future = new CompletableFuture<>();
        getAvatar(new FutureCallback<BufferedImage>() {
            @Override
            public void onSuccess(BufferedImage bufferedImage) {
                future.complete(bufferedImage);
            }

            @Override
            public void onFailure(Throwable throwable) {
                future.complete(null);
            }
        });
        return future;
    }

    @Override
    public void getAvatar(FutureCallback<BufferedImage> callback) {
        Futures.addCallback(api.getThreadPool().getListeningExecutorService().submit(new Callable<BufferedImage>() {
            @Override
            public BufferedImage call() throws Exception {
                byte[] imageAsBytes = getAvatarAsBytearray().get();
                if (imageAsBytes.length == 0) {
                    return null;
                }
                InputStream in = new ByteArrayInputStream(imageAsBytes);
                return ImageIO.read(in);
            }
        }), callback);
    }

    @Override
    public URL getAvatarUrl() {
        if (avatarId == null) {
            return null;
        }
        try {
            return new URL("https://discordapp.com/api/users/" + id + "/avatars/" + avatarId + ".jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
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

    /**
     * Sets the channel id of the user.
     *
     * @param userChannelId The channel id of the user.
     */
    public void setUserChannelId(String userChannelId) {
        this.userChannelId = userChannelId;
    }

    /**
     * Gets the channel id of the user.
     *
     * @return The channel id of the user.
     */
    public String getUserChannelId() {
        return userChannelId;
    }

}
