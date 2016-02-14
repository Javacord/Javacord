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
package de.btobastian.javacord;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.entities.message.impl.ImplMessageHistory;
import de.btobastian.javacord.exceptions.PermissionsException;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.server.ServerJoinListener;
import de.btobastian.javacord.utils.DiscordWebsocket;
import de.btobastian.javacord.utils.ThreadPool;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.util.Base64;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The implementation of {@link DiscordAPI}.
 */
public class ImplDiscordAPI implements DiscordAPI {

    private final ThreadPool pool;

    private String email =  null;
    private String password = null;
    private String token = null;
    private String game = null;
    private boolean idle = false;

    private User you = null;

    private DiscordWebsocket socket = null;

    private final ConcurrentHashMap<String, Server> servers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    private final ArrayList<Message> messages = new ArrayList<>();

    private final ConcurrentHashMap<Class<?>, List<Listener>> listeners = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, SettableFuture<Server>> waitingForListener = new ConcurrentHashMap<>();

    // let the garbage collector delete old histories
    private final Set<MessageHistory> messageHistories =
            Collections.newSetFromMap(new WeakHashMap<MessageHistory, Boolean>());

    private final Object listenerLock = new Object();
    private final ServerJoinListener listener = new ServerJoinListener() {
        @Override
        public void onServerJoin(DiscordAPI api, Server server) {
            synchronized (listenerLock) { // be sure to read the guild id before trying to check for waiting listeners
                SettableFuture<Server> future = waitingForListener.get(server.getId());
                if (future != null) {
                    waitingForListener.remove(server.getId());
                    future.set(server);
                }
            }
        }
    };

    /**
     * Creates a new instance of this class.
     *
     * @param pool The used pool of the library.
     */
    public ImplDiscordAPI(ThreadPool pool) {
        this.pool = pool;
    }

    @Override
    public void connect(FutureCallback<DiscordAPI> callback) {
        final DiscordAPI api = this;
        Futures.addCallback(pool.getListeningExecutorService().submit(new Callable<DiscordAPI>() {
            @Override
            public DiscordAPI call() throws Exception {
                connectBlocking();
                return api;
            }
        }), callback);
    }

    @Override
    public void connectBlocking() {
        if (token == null || !checkTokenBlocking(token)) {
            token = requestTokenBlocking();
        }
        String gateway = requestGatewayBlocking();
        try {
            socket = new DiscordWebsocket(new URI(gateway), this);
            socket.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(SSLContext.getDefault()));
            socket.connect();
        } catch (URISyntaxException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        try {
            if (!socket.isReady().get()) {
                throw new IllegalStateException("Socket closed before ready packet was received!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setGame(String game) {
        this.game = game;
        try {
            if (socket != null && socket.isReady().isDone() && socket.isReady().get()) {
                socket.updateStatus();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getGame() {
        return game;
    }

    @Override
    public Server getServerById(String id) {
        return servers.get(id);
    }

    @Override
    public Collection<Server> getServers() {
        return servers.values();
    }

    @Override
    public Future<User> getUserById(String id) {
        User user = users.get(id);
        if (user != null) {
            return Futures.immediateFuture(user);
        }
        return getThreadPool().getListeningExecutorService().submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                // TODO make request (GET /api/:guild_id/members/:user_id)
                return null;
            }
        });
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void registerListener(Listener listener) {
        for (Class<?> implementedInterface : listener.getClass().getInterfaces()) {
            if (Listener.class.isAssignableFrom(implementedInterface)) {
                List<Listener> listenersList = listeners.get(implementedInterface);
                if (listenersList == null) {
                    listenersList = new ArrayList<>();
                    listeners.put(implementedInterface, listenersList);
                }
                synchronized (listenersList) {
                    listenersList.add(listener);
                }
            }
        }
    }

    @Override
    public Message getMessageById(String id) {
        synchronized (messages) {
            for (Message message : messages) {
                if (message.getId().equals(id)) {
                    return message;
                }
            }
        }
        synchronized (messageHistories) {
            for (MessageHistory history : messageHistories) {
                history.getMessageById(id);
            }
        }
        return null;
    }

    @Override
    public ThreadPool getThreadPool() {
        return pool;
    }

    @Override
    public void setIdle(boolean idle) {
        this.idle = idle;
        try {
            if (socket != null && socket.isReady().isDone() && socket.isReady().get()) {
                socket.updateStatus();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isIdle() {
        return idle;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean checkTokenBlocking(String token) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://discordapp.com/api/users/@me/guilds")
                    .header("authorization", token)
                    .asJson();
            if (response.getStatus() < 200 || response.getStatus() > 299) {
                return false;
            }
            return true;
        } catch (UnirestException e) {
            return false;
        }
    }

    @Override
    public Future<Server> acceptInvite(String inviteCode) {
        return acceptInvite(inviteCode, null);
    }

    @Override
    public Future<Server> acceptInvite(final String inviteCode, FutureCallback<Server> callback) {
        ListenableFuture<Server> future = getThreadPool().getListeningExecutorService().submit(new Callable<Server>() {
            @Override
            public Server call() throws Exception {
                final SettableFuture<Server> settableFuture;
                synchronized (listenerLock) {
                    HttpResponse<JsonNode> response = Unirest.post("https://discordapp.com/api/invite/" + inviteCode)
                            .header("authorization", token)
                            .asJson();
                    if (response.getStatus() == 403) {
                        throw new PermissionsException("Missing permissions!");
                    }
                    if (response.getStatus() < 200 || response.getStatus() > 299) {
                        throw new Exception("Received http status code " + response.getStatus()
                                + " with message " + response.getStatusText());
                    }
                    String guildId = response.getBody().getObject().getJSONObject("guild").getString("id");
                    if (getServerById(guildId) != null) {
                        throw new IllegalStateException("Already member of this server!");
                    }
                    settableFuture = SettableFuture.create();
                    waitingForListener.put(guildId, settableFuture);
                }
                return settableFuture.get();
            }
        });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Server> createServer(String name) {
        return createServer(name, null, null);
    }

    @Override
    public Future<Server> createServer(String name, FutureCallback<Server> callback) {
        return createServer(name, null, callback);
    }

    @Override
    public Future<Server> createServer(String name, BufferedImage icon) {
        return createServer(name, icon, null);
    }

    @Override
    public Future<Server> createServer(final String name, final BufferedImage icon, FutureCallback<Server> callback) {
        ListenableFuture<Server> future = getThreadPool().getListeningExecutorService().submit(new Callable<Server>() {
            @Override
            public Server call() throws Exception {
                if (name == null || name.length() < 2 || name.length() > 100) {
                    throw new IllegalArgumentException("Name must be 2-100 characters long!");
                }
                JSONObject params = new JSONObject();
                if (icon != null) {
                    if (icon.getHeight() != 128 || icon.getWidth() != 128) {
                        throw new IllegalArgumentException("Icon must be 128*128px!");
                    }
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(icon, "jpg", os);
                    params.put("icon", "data:image/jpg;base64," + Base64.encodeBytes(os.toByteArray()));
                }
                params.put("name", name);
                params.put("region", "us-west");
                final SettableFuture<Server> settableFuture;
                synchronized (listenerLock) {
                    HttpResponse<JsonNode> response = Unirest.post("https://discordapp.com/api/guilds")
                            .header("authorization", token)
                            .header("Content-Type", "application/json")
                            .body(params.toString())
                            .asJson();
                    if (response.getStatus() < 200 || response.getStatus() > 299) {
                        throw new IllegalStateException("Received http status code " + response.getStatus()
                                + " with message " + response.getStatusText());
                    }
                    String guildId = response.getBody().getObject().getString("id");
                    settableFuture = SettableFuture.create();
                    waitingForListener.put(guildId, settableFuture);
                }
                return settableFuture.get();
            }
        });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public User getYourself() {
        return you;
    }

    /**
     * Sets yourself.
     *
     * @param user You.
     */
    public void setYourself(User user) {
        this.you = user;
    }

    /**
     * Gets or creates a user based on the given data.
     *
     * @param data The JSONObject containing the data.
     * @return The user.
     */
    public User getOrCreateUser(JSONObject data) {
        String id = data.getString("id");
        User user = users.get(id);
        if (user == null) {
            user = new ImplUser(data, this);
        }
        return user;
    }

    /**
     * Gets the map which contains all known servers.
     *
     * @return The map which contains all known servers.
     */
    public ConcurrentHashMap<String, Server> getServerMap() {
        return servers;
    }

    /**
     * Gets the map which contains all known users.
     *
     * @return The map which contains all known users.
     */
    public ConcurrentHashMap<String, User> getUserMap() {
        return users;
    }

    /**
     * Gets the used websocket.
     *
     * @return The websocket.
     */
    public DiscordWebsocket getSocket() {
        return socket;
    }

    /**
     * Requests a new token.
     *
     * @return The requested token.
     */
    public String requestTokenBlocking() {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://discordapp.com/api/auth/login")
                    .field("email", email)
                    .field("password", password)
                    .asJson();
            JSONObject jsonResponse = response.getBody().getObject();
            if (response.getStatus() == 400) {
                throw new IllegalArgumentException("400 Bad request! Maybe wrong email or password?");
            }
            if (response.getStatus() < 200 || response.getStatus() > 299) {
                throw new IllegalStateException("Received http status code " + response.getStatus()
                        + " with message " + response.getStatusText());
            }
            if (jsonResponse.has("password") || jsonResponse.has("email")) {
                throw new IllegalArgumentException("Wrong email or password!");
            }
            return jsonResponse.getString("token");
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Requests the gateway.
     *
     * @return The requested gateway.
     */
    public String requestGatewayBlocking() {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://discordapp.com/api/gateway")
                    .header("authorization", token)
                    .asJson();
            if (response.getStatus() == 401) {
                throw new IllegalStateException("Cannot request gateway! Invalid token?");
            }
            if (response.getStatus() < 200 || response.getStatus() > 299) {
                throw new IllegalStateException("Received http status code " + response.getStatus()
                        + " with message " + response.getStatusText());
            }
            return response.getBody().getObject().getString("url");
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a list with all registers listeners of the given class.
     *
     * @param listenerClass The type of the listener.
     * @return A list with all registers listeners of the given class.
     */
    public List<Listener> getListeners(Class<?> listenerClass) {
        List<Listener> listenersList = listeners.get(listenerClass);
        return listenersList == null ? new ArrayList<Listener>() : listenersList;
    }

    /**
     * Adds a message to the message cache.
     *
     * @param message The message to add.
     */
    public void addMessage(Message message) {
        synchronized (messages) {
            if (messages.size() > 200) { // only cache the last 200 messages
                messages.remove(0);
            }
            messages.add(message);
        }
    }

    /**
     * Removes a message from the cache.
     *
     * @param message The message to remove.
     */
    public void removeMessage(Message message) {
        synchronized (messages) {
            messages.remove(message);
        }
        synchronized (messageHistories) {
            for (MessageHistory history : messageHistories) {
                ((ImplMessageHistory) history).removeMessage(message.getId());
            }
        }
    }

    /**
     * Adds a history to the history list.
     *
     * @param history The history to add.
     */
    public void addHistory(MessageHistory history) {
        synchronized (messageHistories) {
            messageHistories.add(history);
        }
    }

    /**
     * Checks the response.
     *
     * @param response The response to check.
     * @throws Exception If the response has problems (status code not between 200 and 300).
     */
    public void checkResponse(HttpResponse<JsonNode> response) throws Exception {
        if (response.getStatus() == 403) {
            throw new PermissionsException("Missing permissions!");
        }
        if (response.getStatus() < 200 || response.getStatus() > 299) {
            throw new Exception("Received http status code " + response.getStatus()
                    + " with message " + response.getStatusText() + " and body " + response.getBody());
        }
    }

    /**
     * Gets a set with all message histories.
     *
     * @return A set with all message histories.
     */
    public Set<MessageHistory> getMessageHistories() {
        return messageHistories;
    }

    /**
     * Gets the internal used server join listener (for server creations and invite accepts).
     *
     * @return The internal used server join listener.
     */
    public ServerJoinListener getInternalServerJoinListener() {
        return listener;
    }

}
