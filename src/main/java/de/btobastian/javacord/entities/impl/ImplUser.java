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
import com.mashape.unirest.request.body.MultipartBody;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.entities.message.impl.ImplMessageHistory;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The implementation of the user interface.
 */
public class ImplUser implements User {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplUser.class);

    private final ImplDiscordAPI api;

    private final String id;
    private String name;
    private String avatarId = null;
    private final Object userChannelIdLock = new Object();
    private String userChannelId = null;
    private String game = null;
    private final String discriminator;
    private final boolean bot;
    private UserStatus status = UserStatus.OFFLINE;

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
        } catch (JSONException ignored) { }
        discriminator = data.getString("discriminator");
        bot = data.has("bot") && data.getBoolean("bot");

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
            logger.debug("Sending typing state to user {}", this);
            HttpResponse<JsonNode> response = Unirest
                    .post("https://discordapp.com/api/channels/" + getUserChannelIdBlocking() + "/typing")
                    .header("authorization", api.getToken())
                    .asJson();
            api.checkResponse(response);
            api.checkRateLimit(response, RateLimitType.UNKNOWN, null);
            logger.debug("Sent typing state to user {}", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isYourself() {
        return api.getYourself() == this;
    }

    @Override
    public Future<byte[]> getAvatarAsByteArray() {
        return getAvatarAsByteArray(null);
    }

    @Override
    public Future<byte[]> getAvatarAsByteArray(FutureCallback<byte[]> callback) {
        ListenableFuture<byte[]> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<byte[]>() {
                    @Override
                    public byte[] call() throws Exception {
                        logger.debug("Trying to get avatar from user {}", ImplUser.this);
                        if (avatarId == null) {
                            logger.debug("User {} seems to have no avatar. Returning empty array!", ImplUser.this);
                            return new byte[0];
                        }
                        URL url = new URL("https://discordapp.com/api/users/" + id + "/avatars/" + avatarId + ".jpg");
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = in.read(buf))) {
                            out.write(buf, 0, n);
                        }
                        out.close();
                        in.close();
                        byte[] avatar = out.toByteArray();
                        logger.debug("Got avatar from user {} (size: {})", ImplUser.this, avatar.length);
                        return avatar;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<BufferedImage> getAvatar() {
        return getAvatar(null);
    }

    @Override
    public Future<BufferedImage> getAvatar(FutureCallback<BufferedImage> callback) {
        ListenableFuture<BufferedImage> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<BufferedImage>() {
                    @Override
                    public BufferedImage call() throws Exception {
                        byte[] imageAsBytes = getAvatarAsByteArray().get();
                        if (imageAsBytes.length == 0) {
                            return null;
                        }
                        InputStream in = new ByteArrayInputStream(imageAsBytes);
                        return ImageIO.read(in);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public URL getAvatarUrl() {
        if (avatarId == null) {
            return null;
        }
        try {
            return new URL("https://discordapp.com/api/users/" + id + "/avatars/" + avatarId + ".jpg");
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public String getAvatarId() {
        return avatarId;
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
                        logger.debug("Trying to send message to user {} (content: \"{}\", tts: {})",
                                ImplUser.this, content, tts);
                        api.checkRateLimit(null, RateLimitType.PRIVATE_MESSAGE, null);
                        HttpResponse<JsonNode> response =
                                Unirest.post("https://discordapp.com/api/channels/"
                                        + getUserChannelIdBlocking() + "/messages")
                                        .header("authorization", api.getToken())
                                        .header("content-type", "application/json")
                                        .body(new JSONObject()
                                                .put("content", content)
                                                .put("tts", tts)
                                                .put("mentions", new String[0]).toString())
                                        .asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.PRIVATE_MESSAGE, null);
                        logger.debug("Sent message to user {} (content: \"{}\", tts: {})", ImplUser.this, content, tts);
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
        return sendFile(file, null, null);
    }

    @Override
    public Future<Message> sendFile(final File file, FutureCallback<Message> callback) {
        return sendFile(file, null, callback);
    }

    @Override
    public Future<Message> sendFile(InputStream inputStream, String filename) {
        return sendFile(inputStream, filename, null, null);
    }

    @Override
    public Future<Message> sendFile(InputStream inputStream, String filename, FutureCallback<Message> callback) {
        return sendFile(inputStream, filename, null, callback);
    }

    @Override
    public Future<Message> sendFile(File file, String comment) {
        return sendFile(file, comment, null);
    }

    @Override
    public Future<Message> sendFile(final File file, final String comment, FutureCallback<Message> callback) {
        final MessageReceiver receiver = this;
        ListenableFuture<Message> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        logger.debug("Trying to send a file to user {} (name: {}, comment: {})",
                                ImplUser.this, file.getName(), comment);
                        api.checkRateLimit(null, RateLimitType.PRIVATE_MESSAGE, null);
                        MultipartBody body = Unirest
                                .post("https://discordapp.com/api/channels/" + getUserChannelIdBlocking() + "/messages")
                                .header("authorization", api.getToken())
                                .field("file", file);
                        if (comment != null) {
                            body.field("content", comment);
                        }
                        HttpResponse<JsonNode> response = body.asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.PRIVATE_MESSAGE, null);
                        logger.debug("Sent a file to user {} (name: {}, comment: {})",
                                ImplUser.this, file.getName(), comment);
                        return new ImplMessage(response.getBody().getObject(), api, receiver);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Message> sendFile(InputStream inputStream, String filename, String comment) {
        return sendFile(inputStream, filename, comment, null);
    }

    @Override
    public Future<Message> sendFile(final InputStream inputStream, final String filename, final String comment,
                                    FutureCallback<Message> callback) {
        final MessageReceiver receiver = this;
        ListenableFuture<Message> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        logger.debug("Trying to send an input stream to user {} (comment: {})",
                                ImplUser.this, comment);
                        api.checkRateLimit(null, RateLimitType.PRIVATE_MESSAGE, null);
                        MultipartBody body = Unirest
                                .post("https://discordapp.com/api/channels/" + getUserChannelIdBlocking() + "/messages")
                                .header("authorization", api.getToken())
                                .field("file", inputStream, filename);
                        if (comment != null) {
                            body.field("content", comment);
                        }
                        HttpResponse<JsonNode> response = body.asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.PRIVATE_MESSAGE, null);
                        logger.debug("Sent an input stream to user {} (comment: {})", ImplUser.this, comment);
                        return new ImplMessage(response.getBody().getObject(), api, receiver);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Collection<Role> getRoles(Server server) {
        Collection<Role> userRoles = new ArrayList<>();
        Iterator<Role> rolesIterator = server.getRoles().iterator();
        while (rolesIterator.hasNext()) {
            Role role = rolesIterator.next();
            if (role.getUsers().contains(this)) {
                userRoles.add(role);
            }
        }
        return userRoles;
    }

    @Override
    public String getGame() {
        return game;
    }

    @Override
    public Future<MessageHistory> getMessageHistory(int limit) {
        return getMessageHistory(null, false, limit, null);
    }

    @Override
    public Future<MessageHistory> getMessageHistory(int limit, FutureCallback<MessageHistory> callback) {
        return getMessageHistory(null, false, limit, callback);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryBefore(Message before, int limit) {
        return getMessageHistory(before.getId(), true, limit, null);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryBefore(
            Message before, int limit, FutureCallback<MessageHistory> callback) {
        return getMessageHistory(before.getId(), true, limit, callback);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryBefore(String beforeId, int limit) {
        return getMessageHistory(beforeId, true, limit, null);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryBefore(
            String beforeId, int limit, FutureCallback<MessageHistory> callback) {
        return getMessageHistory(beforeId, true, limit, callback);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryAfter(Message after, int limit) {
        return getMessageHistory(after.getId(), false, limit, null);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryAfter(
            Message after, int limit, FutureCallback<MessageHistory> callback) {
        return getMessageHistory(after.getId(), false, limit, callback);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryAfter(String afterId, int limit) {
        return getMessageHistory(afterId, false, limit, null);
    }

    @Override
    public Future<MessageHistory> getMessageHistoryAfter(
            String afterId, int limit, FutureCallback<MessageHistory> callback) {
        return getMessageHistory(afterId, false, limit, callback);
    }

    @Override
    public String getMentionTag() {
        return "<@" + getId() + ">";
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the user.
     *
     * @param status The status of the user.
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Gets the message history.
     *
     * @param messageId Gets the messages before or after the message with the given id.
     * @param before Whether it should get the messages before or after the given message.
     * @param limit The maximum number of messages.
     * @param callback The callback.
     * @return The history.
     */
    private Future<MessageHistory> getMessageHistory(
            final String messageId, final boolean before, final int limit, FutureCallback<MessageHistory> callback) {
        ListenableFuture<MessageHistory> future = api.getThreadPool().getListeningExecutorService().submit(
                new Callable<MessageHistory>() {
                    @Override
                    public MessageHistory call() throws Exception {
                        MessageHistory history =
                                new ImplMessageHistory(api, getUserChannelIdBlocking(), messageId, before, limit);
                        api.addHistory(history);
                        return history;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    /**
     * Sets the channel id of the user.
     *
     * @param userChannelId The channel id of the user.
     */
    public void setUserChannelId(String userChannelId) {
        synchronized (userChannelIdLock) {
            this.userChannelId = userChannelId;
        }
    }

    /**
     * Gets the channel id of the user.
     * Requests it if there was no communication before.
     *
     * @return The channel id of the user.
     * @throws Exception If can not request channel id.
     */
    public String getUserChannelIdBlocking() throws Exception {
        synchronized (userChannelIdLock) {
            if (userChannelId != null) {
                return userChannelId;
            }
            logger.debug("Trying to get channel id of user {}", ImplUser.this);
            HttpResponse<JsonNode> response = Unirest
                    .post("https://discordapp.com/api/users/" + api.getYourself().getId() + "/channels")
                    .header("authorization", api.getToken())
                    .header("Content-Type", "application/json")
                    .body(new JSONObject().put("recipient_id", id).toString())
                    .asJson();
            api.checkResponse(response);
            api.checkRateLimit(response, RateLimitType.UNKNOWN, null);
            userChannelId = response.getBody().getObject().getString("id");
            logger.debug("Got channel id of user {} (channel id: {})", ImplUser.this, userChannelId);
            return userChannelId;
        }
    }

    /**
     * Gets the channel id of the user.
     * Will be null if there was no communication before.
     *
     * @return The channel id of the user.
     */
    public String getUserChannelId() {
        synchronized (userChannelIdLock) {
            return userChannelId;
        }
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the game of the user.
     *
     * @param game The game to set.
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * Sets the avatar id of the user.
     *
     * @param avatarId The avatar id of the user.
     */
    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    @Override
    public String toString() {
        return getName() + " (id: " + getId() + ")";
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
