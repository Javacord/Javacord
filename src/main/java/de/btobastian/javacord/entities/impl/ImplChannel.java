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
import com.mashape.unirest.request.body.MultipartBody;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.InviteBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.entities.message.impl.ImplMessageHistory;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.listener.channel.ChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener;
import de.btobastian.javacord.listener.channel.ChannelDeleteListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * The implementation of the channel interface.
 */
public class ImplChannel implements Channel {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplChannel.class);

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);

    private final ImplDiscordAPI api;

    private final String id;
    private String name;
    private String topic = null;
    private int position;
    private final ImplServer server;

    private final ConcurrentHashMap<String, Permissions> overwrittenPermissions = new ConcurrentHashMap<>();

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
                overwrittenPermissions.put(id, new ImplPermissions(allow, deny));
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
                    logger.debug("Trying to delete channel {}", ImplChannel.this);
                    HttpResponse<JsonNode> response = Unirest
                            .delete("https://discordapp.com/api/channels/" + id)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    server.removeChannel(ImplChannel.this);
                    logger.info("Deleted channel {}", ImplChannel.this);
                    // call listener
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<ChannelDeleteListener> listeners = api.getListeners(ChannelDeleteListener.class);
                            synchronized (listeners) {
                                for (ChannelDeleteListener listener : listeners) {
                                    listener.onChannelDelete(api, ImplChannel.this);
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
            logger.debug("Sending typing state in channel {}", this);
            Unirest.post("https://discordapp.com/api/channels/" + id + "/typing")
                    .header("authorization", api.getToken())
                    .asJson();
            logger.debug("Sent typing state in channel {}", this);
        } catch (UnirestException e) {
            logger.warn("Couldn't send typing state in channel {}. Please contact the developer!", this, e);
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
                        logger.debug("Trying to send message in channel {} (content: \"{}\", tts: {})",
                                ImplChannel.this, content, tts);
                        api.checkRateLimit(null, RateLimitType.SERVER_MESSAGE, server);
                        HttpResponse<JsonNode> response =
                                Unirest.post("https://discordapp.com/api/channels/" + id + "/messages")
                                        .header("authorization", api.getToken())
                                        .header("content-type", "application/json")
                                        .body(new JSONObject()
                                                .put("content", content)
                                                .put("tts", tts)
                                                .put("mentions", new String[0]).toString())
                                        .asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.SERVER_MESSAGE, server);
                        logger.debug("Sent message in channel {} (content: \"{}\", tts: {})",
                                ImplChannel.this, content, tts);
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
                        logger.debug("Trying to send a file in channel {} (name: {}, comment: {})",
                                ImplChannel.this, file.getName(), comment);
                        api.checkRateLimit(null, RateLimitType.SERVER_MESSAGE, server);
                        MultipartBody body = Unirest
                                .post("https://discordapp.com/api/channels/" + id + "/messages")
                                .header("authorization", api.getToken())
                                .field("file", file);
                        if (comment != null) {
                            body.field("content", comment);
                        }
                        HttpResponse<JsonNode> response = body.asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.SERVER_MESSAGE, server);
                        logger.debug("Sent a file in channel {} (name: {}, comment: {})",
                                ImplChannel.this, file.getName(), comment);
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
                        logger.debug("Trying to send an input stream in channel {} (comment: {})",
                                ImplChannel.this, comment);
                        api.checkRateLimit(null, RateLimitType.SERVER_MESSAGE, server);
                        MultipartBody body = Unirest
                                .post("https://discordapp.com/api/channels/" + id + "/messages")
                                .header("authorization", api.getToken())
                                .field("file", inputStream, filename);
                        if (comment != null) {
                            body.field("content", comment);
                        }
                        HttpResponse<JsonNode> response = body.asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.SERVER_MESSAGE, server);
                        logger.debug("Sent an input stream in channel {} (comment: {})", ImplChannel.this, comment);
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
    public Future<Exception> updateName(String newName) {
        return update(newName, getTopic());
    }

    @Override
    public Future<Exception> updateTopic(String newTopic) {
        return update(getName(), newTopic);
    }

    @Override
    public Future<Exception> update(final String newName, final String newTopic) {
        final JSONObject params = new JSONObject()
                .put("name", newName)
                .put("topic", newTopic);
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                logger.debug("Trying to update channel {} (new name: {}, old name: {}, new topic: {}, old topic: {})",
                        ImplChannel.this, newName, getName(), newTopic, getTopic());
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .patch("https://discordapp.com/api/channels/" + getId())
                            .header("authorization", api.getToken())
                            .header("Content-Type", "application/json")
                            .body(params.toString())
                            .asJson();
                    api.checkResponse(response);
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, server);
                    logger.info("Updated channel {} (new name: {}, old name: {}, new topic: {}, old topic: {})",
                            ImplChannel.this, newName, getName(), newTopic, getTopic());
                    String updatedName = response.getBody().getObject().getString("name");
                    String updatedTopic = null;
                    if (response.getBody().getObject().has("topic")
                            && !response.getBody().getObject().isNull("topic")) {
                        updatedTopic = response.getBody().getObject().getString("topic");
                    }

                    // check name
                    if (!updatedName.equals(getName())) {
                        final String oldName = getName();
                        setName(updatedName);
                        api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                            @Override
                            public void run() {
                                List<ChannelChangeNameListener> listeners =
                                        api.getListeners(ChannelChangeNameListener.class);
                                synchronized (listeners) {
                                    for (ChannelChangeNameListener listener : listeners) {
                                        try {
                                            listener.onChannelChangeName(api, ImplChannel.this, oldName);
                                        } catch (Throwable t) {
                                            logger.warn("Uncaught exception in ChannelChangeNameListener!", t);
                                        }
                                    }
                                }
                            }
                        });
                    }

                    // check topic
                    if ((getTopic() != null && updatedTopic == null) || (getTopic() == null && updatedTopic != null)
                            || (getTopic() != null && !getTopic().equals(updatedTopic))) {
                        final String oldTopic = getTopic();
                        setTopic(updatedTopic);
                        api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                            @Override
                            public void run() {
                                List<ChannelChangeTopicListener> listeners =
                                        api.getListeners(ChannelChangeTopicListener.class);
                                synchronized (listeners) {
                                    for (ChannelChangeTopicListener listener : listeners) {
                                        try {
                                            listener.onChannelChangeTopic(api, ImplChannel.this, oldTopic);
                                        } catch (Throwable t) {
                                            logger.warn("Uncaught exception in ChannelChangeTopicListener!", t);
                                        }
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        });
    }

    @Override
    public String getMentionTag() {
        return "<#" + getId() + ">";
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
                                new ImplMessageHistory(api, id, messageId, before, limit);
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
    public String toString() {
        return getName() + " (id: " + getId() + ")";
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
