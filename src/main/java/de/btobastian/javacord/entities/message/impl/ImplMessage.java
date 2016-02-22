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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.entities.message.MessageReceiver;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.message.MessageDeleteListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The implementation of the user interface.
 */
public class ImplMessage implements Message {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final SimpleDateFormat FORMAT_ALTERNATIVE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat FORMAT_ALTERNATIVE_TWO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    private final ImplDiscordAPI api;

    private final String id;
    private String content = null;
    private final boolean tts;
    private final User author;
    private final List<User> mentions = new ArrayList<>();
    private final MessageReceiver receiver;
    private final String channelId;
    private final List<MessageAttachment> attachments = new ArrayList<>();
    private Calendar creationDate = Calendar.getInstance();

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     * @param api The api of this server.
     */
    public ImplMessage(JSONObject data, ImplDiscordAPI api, MessageReceiver receiver) {
        this.api = api;

        id = data.getString("id");
        if (data.has("content")) {
            content = data.getString("content");
        }
        tts = data.getBoolean("tts");

        if (data.has("timestamp")) {
            String time = data.getString("timestamp");
            Calendar calendar = Calendar.getInstance();
            synchronized (FORMAT) { // SimpleDateFormat#parse() isn't thread safe...
                try {
                    calendar.setTime(FORMAT.parse(time.substring(0, time.length() - 9)));
                } catch (ParseException ignored) {
                    try {
                        calendar.setTime(FORMAT_ALTERNATIVE.parse(time.substring(0, time.length() - 9)));
                    } catch (ParseException ignored2) {
                        try {
                            calendar.setTime(FORMAT_ALTERNATIVE_TWO.parse(time.substring(0, time.length() - 9)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            creationDate = calendar;
        }
        author = api.getOrCreateUser(data.getJSONObject("author"));

        try {
            JSONArray attachments = data.getJSONArray("attachments");
            for (int i = 0; i < attachments.length(); i++) {
                JSONObject attachment = attachments.getJSONObject(i);
                String url = attachment.getString("url");
                String proxyUrl = attachment.getString("proxy_url");
                int size = attachment.getInt("size");
                String id = attachment.getString("id");
                String name = attachment.getString("filename");
                this.attachments.add(new ImplMessageAttachment(url, proxyUrl, size, id, name));
            }
        } catch (JSONException ignored) { }

        JSONArray mentions = data.getJSONArray("mentions");
        for (int i = 0; i < mentions.length(); i++) {
            String userId = mentions.getJSONObject(i).getString("id");
            User user;
            try {
                user = api.getUserById(userId).get();
            } catch (InterruptedException | ExecutionException e) {
                continue;
            }
            this.mentions.add(user);
        }

        channelId = data.getString("channel_id");
        if (receiver == null) {
            this.receiver = findReceiver(channelId);
        } else {
            this.receiver = receiver;
        }

        if (getChannelReceiver() != null) {
            ((ImplServer) getChannelReceiver().getServer()).addMember(author);
        }

        api.addMessage(this);
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
        final Message message = this;
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    HttpResponse<JsonNode> response = Unirest.delete
                            ("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId())
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    api.removeMessage(message);
                    // call listener
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(MessageDeleteListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((MessageDeleteListener) listener).onMessageDelete(api, message);
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
    public Collection<MessageAttachment> getAttachments() {
        return Collections.unmodifiableCollection(attachments);
    }

    @Override
    public Future<Message> reply(String content) {
        return reply(content, false);
    }

    @Override
    public Future<Message> reply(String content, boolean tts) {
        return reply(content, tts, null);
    }

    @Override
    public Future<Message> reply(String content, FutureCallback<Message> callback) {
        return reply(content, false, callback);
    }

    @Override
    public Future<Message> reply(final String content, final boolean tts, FutureCallback<Message> callback) {
        final MessageReceiver receiver = findReceiver(channelId);
        ListenableFuture<Message> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        api.checkRateLimit();
                        HttpResponse<JsonNode> response =
                                Unirest.post("https://discordapp.com/api/channels/" + channelId + "/messages")
                                        .header("authorization", api.getToken())
                                        .header("content-type", "application/json")
                                        .body(new JSONObject()
                                                .put("content", content)
                                                .put("tts", tts)
                                                .put("mentions", new String[0]).toString())
                                        .asJson();
                        api.checkResponse(response);
                        return new ImplMessage(response.getBody().getObject(), api, receiver);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Message> replyFile(final File file) {
        return replyFile(file, null);
    }

    @Override
    public Future<Message> replyFile(final File file, FutureCallback<Message> callback) {
        final MessageReceiver receiver = findReceiver(channelId);
        ListenableFuture<Message> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        api.checkRateLimit();
                        HttpResponse<JsonNode> response =
                                Unirest.post("https://discordapp.com/api/channels/" + channelId + "/messages")
                                        .header("authorization", api.getToken())
                                        .field("file", file)
                                        .asJson();
                        api.checkResponse(response);
                        return new ImplMessage(response.getBody().getObject(), api, receiver);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Calendar getCreationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate.getTime());
        return calendar;
    }

    @Override
    public int compareTo(Message other) {
        return this.creationDate.compareTo(other.getCreationDate());
    }

    /**
     * Updates the content of the message.
     *
     * @param content The new content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Tries to find the message receiver based on its channel id.
     *
     * @param channelId The channel id of the receiver.
     * @return The receiver with the given id.
     */
    private MessageReceiver findReceiver(String channelId) {
        for (Server server : api.getServers()) {
            if (server.getChannelById(channelId) != null) {
                return server.getChannelById(channelId);
            }
        }
        for (User user : api.getUsers()) {
            if (channelId.equals(((ImplUser) user).getUserChannelId())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getAuthor().getName() + ": " + getContent() + " (id: " + getId() + ")";
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
