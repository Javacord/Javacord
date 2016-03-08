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
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.*;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelCreateListener;
import de.btobastian.javacord.listener.role.RoleCreateListener;
import de.btobastian.javacord.listener.server.*;
import de.btobastian.javacord.listener.user.UserRoleAddListener;
import de.btobastian.javacord.listener.user.UserRoleRemoveListener;
import de.btobastian.javacord.listener.voicechannel.VoiceChannelCreateListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The implementation of the server interface.
 */
public class ImplServer implements Server {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(ImplServer.class);

    private final ImplDiscordAPI api;

    private final ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, VoiceChannel> voiceChannels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> members = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Role> roles = new ConcurrentHashMap<>();

    private final String id;
    private String name;
    private Region region;
    private int memberCount;
    private boolean large;

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
        region = Region.getRegionByKey(data.getString("region"));
        memberCount = data.getInt("member_count");
        large = data.getBoolean("large");

        JSONArray roles = data.getJSONArray("roles");
        for (int i = 0; i < roles.length(); i++) {
            new ImplRole(roles.getJSONObject(i), this, api);
        }

        JSONArray channels = data.getJSONArray("channels");
        for (int i = 0; i < channels.length(); i++) {
            JSONObject channelJson = channels.getJSONObject(i);
            String type = channelJson.getString("type");
            if (type.equals("text")) {
                new ImplChannel(channels.getJSONObject(i), this, api);
            }
            if (type.equals("voice")) {
                new ImplVoiceChannel(channels.getJSONObject(i), this, api);
            }
        }

        JSONArray members = new JSONArray();
        if (data.has("members")) {
            members = data.getJSONArray("members");
        }
        for (int i = 0; i < members.length(); i++) {
            User member = api.getOrCreateUser(members.getJSONObject(i).getJSONObject("user"));
            this.members.put(member.getId(), member);

            JSONArray memberRoles = members.getJSONObject(i).getJSONArray("roles");
            for (int j = 0; j < memberRoles.length(); j++) {
                Role role = getRoleById(memberRoles.getString(j));
                if (role != null) {
                    ((ImplRole) role).addUserNoUpdate(member);
                }
            }
        }

        JSONArray presences = new JSONArray();
        if (data.has("presences")) {
            presences = data.getJSONArray("presences");
        }
        for (int i = 0; i < presences.length(); i++) {
            JSONObject presence = presences.getJSONObject(i);
            User user;
            try {
                user = api.getUserById(presence.getJSONObject("user").getString("id")).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                continue;
            }
            if (presence.has("game") && !presence.isNull("game")) {
                if (presence.getJSONObject("game").has("name") && !presence.getJSONObject("game").isNull("name")) {
                    ((ImplUser) user).setGame(presence.getJSONObject("game").getString("name"));
                }
            }
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
    public Future<Exception> delete() {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    logger.debug("Trying to delete server {}", ImplServer.this);
                    HttpResponse<JsonNode> response = Unirest.delete("https://discordapp.com/api/guilds/" + id)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    api.getServerMap().remove(id);
                    logger.info("Deleted server {}", ImplServer.this);
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(ServerLeaveListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((ServerLeaveListener) listener).onServerLeave(api, ImplServer.this);
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
    public Future<Exception> leave() {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    logger.debug("Trying to leave server {}", ImplServer.this);
                    HttpResponse<JsonNode> response = Unirest.delete("https://discordapp.com/api/users/@me/guilds/" + id)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    api.getServerMap().remove(id);
                    logger.info("Left server {}", ImplServer.this);
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(ServerLeaveListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((ServerLeaveListener) listener).onServerLeave(api, ImplServer.this);
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
    public Channel getChannelById(String id) {
        return channels.get(id);
    }

    @Override
    public Collection<Channel> getChannels() {
        return Collections.unmodifiableCollection(channels.values());
    }

    @Override
    public VoiceChannel getVoiceChannelById(String id) {
        return voiceChannels.get(id);
    }

    @Override
    public Collection<VoiceChannel> getVoiceChannels() {
        return Collections.unmodifiableCollection(voiceChannels.values());
    }

    @Override
    public User getMemberById(String id) {
        return members.get(id);
    }

    @Override
    public Collection<User> getMembers() {
        return Collections.unmodifiableCollection(members.values());
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user.getId());
    }

    @Override
    public boolean isMember(String userId) {
        return members.containsKey(userId);
    }

    @Override
    public Collection<Role> getRoles() {
        return Collections.unmodifiableCollection(roles.values());
    }

    @Override
    public Role getRoleById(String id) {
        return roles.get(id);
    }

    @Override
    public Future<Channel> createChannel(String name) {
        return createChannel(name, null);
    }

    @Override
    public Future<Channel> createChannel(final String name, FutureCallback<Channel> callback) {
        ListenableFuture<Channel> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Channel>() {
                    @Override
                    public Channel call() throws Exception {
                        final Channel channel = (Channel) createChannelBlocking(name, false);
                        logger.info("Created channel in server {} (name: {}, voice: {}, id: {})",
                                ImplServer.this, channel.getName(), false, channel.getId());
                        api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                            @Override
                            public void run() {
                                List<Listener> listeners =  api.getListeners(ChannelCreateListener.class);
                                synchronized (listeners) {
                                    for (Listener listener : listeners) {
                                        ((ChannelCreateListener) listener).onChannelCreate(api, channel);
                                    }
                                }
                            }
                        });
                        return channel;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<VoiceChannel> createVoiceChannel(String name) {
        return createVoiceChannel(name, null);
    }

    @Override
    public Future<VoiceChannel> createVoiceChannel(final String name, FutureCallback<VoiceChannel> callback) {
        ListenableFuture<VoiceChannel> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<VoiceChannel>() {
                    @Override
                    public VoiceChannel call() throws Exception {
                        final VoiceChannel channel = (VoiceChannel) createChannelBlocking(name, true);
                        logger.info("Created channel in server {} (name: {}, voice: {}, id: {})",
                                ImplServer.this, channel.getName(), true, channel.getId());
                        api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                            @Override
                            public void run() {
                                List<Listener> listeners =  api.getListeners(VoiceChannelCreateListener.class);
                                synchronized (listeners) {
                                    for (Listener listener : listeners) {
                                        ((VoiceChannelCreateListener) listener).onVoiceChannelCreate(api, channel);
                                    }
                                }
                            }
                        });
                        return channel;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Invite[]> getInvites() {
        return getInvites(null);
    }

    @Override
    public Future<Invite[]> getInvites(FutureCallback<Invite[]> callback) {
        ListenableFuture<Invite[]> future = api.getThreadPool().getListeningExecutorService().submit(
                new Callable<Invite[]>() {
                    @Override
                    public Invite[] call() throws Exception {
                        logger.debug("Trying to get invites for server {}", ImplServer.this);
                        HttpResponse<JsonNode> response = Unirest
                                .get("https://discordapp.com/api/guilds/" + getId() + "/invites")
                                .header("authorization", api.getToken())
                                .asJson();
                        api.checkResponse(response);
                        Invite[] invites = new Invite[response.getBody().getArray().length()];
                        for (int i = 0; i < response.getBody().getArray().length(); i++) {
                            invites[i] = new ImplInvite(api, response.getBody().getArray().getJSONObject(i));
                        }
                        logger.debug("Got invites for server {} (amount: {})", ImplServer.this, invites.length);
                        return invites;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Exception> updateRoles(final User user, final Role[] roles) {
        final String[] roleIds = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleIds[i] = roles[i].getId();
        }
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                logger.debug("Trying to update roles in server {} (amount: {})", ImplServer.this, roles.length);
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .patch("https://discordapp.com/api/guilds/" + getId() + "/members/" + user.getId())
                            .header("authorization", api.getToken())
                            .header("Content-Type", "application/json")
                            .body(new JSONObject().put("roles", roleIds).toString())
                            .asJson();
                    api.checkResponse(response);
                    for (final Role role : user.getRoles(ImplServer.this)) {
                        boolean contains = false;
                        for (Role r : roles) {
                            if (role == r) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            ((ImplRole) role).removeUserNoUpdate(user);
                            api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                                @Override
                                public void run() {
                                    List<Listener> listeners =  api.getListeners(UserRoleRemoveListener.class);
                                    synchronized (listeners) {
                                        for (Listener listener : listeners) {
                                            ((UserRoleRemoveListener) listener).onUserRoleRemove(api, user, role);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    for (final Role role : roles) {
                        if (!user.getRoles(ImplServer.this).contains(role)) {
                            ((ImplRole) role).addUserNoUpdate(user);
                            api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                                @Override
                                public void run() {
                                    List<Listener> listeners =  api.getListeners(UserRoleAddListener.class);
                                    synchronized (listeners) {
                                        for (Listener listener : listeners) {
                                            ((UserRoleAddListener) listener).onUserRoleAdd(api, user, role);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    logger.debug("Updated roles in server {} (amount: {})", ImplServer.this, getRoles().size());
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        });
    }

    @Override
    public Future<Exception> banUser(User user) {
        return banUser(user.getId(), 0);
    }

    @Override
    public Future<Exception> banUser(String userId) {
        return banUser(userId, 0);
    }

    @Override
    public Future<Exception> banUser(User user, int deleteDays) {
        return banUser(user.getId(), deleteDays);
    }

    @Override
    public Future<Exception> banUser(final String userId, final int deleteDays) {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    logger.debug("Trying to ban an user from server {} (user id: {}, delete days: {})",
                            ImplServer.this, userId, deleteDays);
                    HttpResponse<JsonNode> response = Unirest
                            .put("https://discordapp.com/api/guilds/:guild_id/bans/" + userId
                                    + "?delete-message-days=" + deleteDays)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    final User user = api.getUserById(userId).get();
                    if (user != null) {
                        removeMember(user);
                    }
                    logger.info("Banned an user from server {} (user id: {}, delete days: {})",
                            ImplServer.this, userId, deleteDays);
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(ServerMemberBanListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((ServerMemberBanListener) listener).onServerMemberBan(api, user, ImplServer.this);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        });
    }

    @Override
    public Future<Exception> unbanUser(final String userId) {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                logger.debug("Trying to unban an user from server {} (user id: {})", ImplServer.this, userId);
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .delete("https://discordapp.com/api/guilds/" + getId() + "/bans/" + userId)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    logger.info("Unbanned an user from server {} (user id: {})", ImplServer.this, userId);
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(ServerMemberUnbanListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((ServerMemberUnbanListener) listener)
                                            .onServerMemberUnban(api, userId, ImplServer.this);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        });
    }

    @Override
    public Future<User[]> getBans() {
        return getBans(null);
    }

    @Override
    public Future<User[]> getBans(FutureCallback<User[]> callback) {
        ListenableFuture<User[]> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<User[]>() {
                    @Override
                    public User[] call() throws Exception {
                        logger.debug("Trying to get bans for server {}", ImplServer.this);
                        HttpResponse<JsonNode> response = Unirest
                                .get("https://discordapp.com/api/guilds/" + getId() + "/bans")
                                .header("authorization", api.getToken())
                                .asJson();
                        api.checkResponse(response);
                        JSONArray bannedUsersJson = response.getBody().getArray();
                        User[] bannedUsers = new User[bannedUsersJson.length()];
                        for (int i = 0; i < bannedUsersJson.length(); i++) {
                            bannedUsers[i] = api.getOrCreateUser(bannedUsersJson.getJSONObject(i));
                        }
                        logger.debug("Got bans for server {} (amount: {})", ImplServer.this, bannedUsers.length);
                        return bannedUsers;
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Exception> kickUser(User user) {
        return kickUser(user.getId());
    }

    @Override
    public Future<Exception> kickUser(final String userId) {
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                logger.debug("Trying to kick an user from server {} (user id: {})", ImplServer.this);
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .delete("https://discordapp.com/api/guilds/"+ getId() + "/members/" + userId)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    final User user = api.getUserById(userId).get();
                    if (user != null) {
                        removeMember(user);
                    }
                    logger.info("Kicked an user from server {} (user id: {})", ImplServer.this);
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<Listener> listeners =  api.getListeners(ServerMemberRemoveListener.class);
                            synchronized (listeners) {
                                for (Listener listener : listeners) {
                                    ((ServerMemberRemoveListener) listener)
                                            .onServerMemberRemove(api, user, ImplServer.this);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        });
    }

    @Override
    public Future<Role> createRole() {
        return createRole(null);
    }

    @Override
    public Future<Role> createRole(FutureCallback<Role> callback) {
        ListenableFuture<Role> future = api.getThreadPool().getListeningExecutorService().submit(new Callable<Role>() {
            @Override
            public Role call() throws Exception {
                logger.debug("Trying to create a role in server {}", ImplServer.this);
                HttpResponse<JsonNode> response = Unirest
                        .post("https://discordapp.com/api/guilds/" + getId() + "/roles")
                        .header("authorization", api.getToken())
                        .asJson();
                api.checkResponse(response);
                final Role role = new ImplRole(response.getBody().getObject(), ImplServer.this, api);
                logger.info("Created role in server {} (name: {}, id: {})",
                        ImplServer.this, role.getName(), role.getId());
                api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                    @Override
                    public void run() {
                        List<Listener> listeners =  api.getListeners(RoleCreateListener.class);
                        synchronized (listeners) {
                            for (Listener listener : listeners) {
                                ((RoleCreateListener) listener).onRoleCreate(api, role);
                            }
                        }
                    }
                });
                return role;
            }
        });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

    @Override
    public Future<Exception> updateName(String newName) {
        return update(newName, null, null);
    }

    @Override
    public Future<Exception> updateRegion(Region newRegion) {
        return update(null, newRegion, null);
    }

    @Override
    public Future<Exception> updateIcon(BufferedImage newIcon) {
        return update(null, null, newIcon);
    }

    @Override
    public Future<Exception> update(final String newName, final Region newRegion, BufferedImage newIcon) {
        final JSONObject params = new JSONObject();
        if (newName == null) {
            params.put("name", getName());
        } else {
            params.put("name", newName);
        }
        if (newRegion != null) {
            params.put("region", newRegion.getKey());
        }

        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                try {
                    logger.debug(
                            "Trying to update server {} (new name: {}, old name: {}, new region: {}, old region: {}",
                            ImplServer.this, newName, getName(), newRegion.getKey(), getRegion().getKey());
                    HttpResponse<JsonNode> response = Unirest
                            .patch("https://discordapp.com/api/guilds/" + getId())
                            .header("authorization", api.getToken())
                            .header("Content-Type", "application/json")
                            .body(params.toString())
                            .asJson();
                    api.checkResponse(response);
                    logger.debug("Updated server {} (new name: {}, old name: {}, new region: {}, old region: {}",
                            ImplServer.this, newName, getName(), newRegion.getKey(), getRegion().getKey());

                    String name = response.getBody().getObject().getString("name");
                    if (!getName().equals(name)) {
                        final String oldName = getName();
                        api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                            @Override
                            public void run() {
                                List<Listener> listeners =  api.getListeners(ServerChangeNameListener.class);
                                synchronized (listeners) {
                                    for (Listener listener : listeners) {
                                        ((ServerChangeNameListener) listener)
                                                .onServerChangeName(api, ImplServer.this, oldName);
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
    public Region getRegion() {
        return region;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public boolean isLarge() {
        return large;
    }

    /**
     * Sets the name of the server.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the region of the server.
     *
     * @param region The region to set.
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Adds a user to the server.
     *
     * @param user The user to add.
     */
    public void addMember(User user) {
        members.put(user.getId(), user);
    }

    /**
     * Removes a user from the server.
     *
     * @param user The user to remove.
     */
    public void removeMember(User user) {
        members.remove(user.getId());
    }

    /**
     * Increments the member count.
     */
    public void incrementMemberCount() {
        memberCount++;
    }

    /**
     * Decrement the member count.
     */
    public void decrementMemberCount() {
        memberCount--;
    }

    /**
     * Sets the member count.
     *
     * @param memberCount The member count to set.
     */
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * Adds a channel to the server.
     *
     * @param channel The channel to add.
     */
    public void addChannel(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    /**
     * Adds a voice channel to the server.
     *
     * @param channel The voice channel to add.
     */
    public void addVoiceChannel(VoiceChannel channel) {
        voiceChannels.put(channel.getId(), channel);
    }

    /**
     * Adds a role to the server.
     *
     * @param role The role to add.
     */
    public void addRole(Role role) {
        roles.put(role.getId(), role);
    }

    /**
     * Removes a role from the server.
     *
     * @param role The role to remove.
     */
    public void removeRole(Role role) {
        roles.remove(role.getId());
    }

    /**
     * Removes a channel from the server.
     *
     * @param channel The channel to remove.
     */
    public void removeChannel(Channel channel) {
        channels.remove(channel.getId());
    }

    /**
     * Removes a voice channel from the server.
     *
     * @param channel The voice channel to remove.
     */
    public void removeVoiceChannel(VoiceChannel channel) {
        voiceChannels.remove(channel.getId());
    }

    /**
     * Creates a new channel.
     *
     * @param name The name of the channel.
     * @param voice Whether the channel should be voice or text.
     * @return The created channel.
     * @throws Exception If something went wrong.
     */
    private Object createChannelBlocking(String name, boolean voice) throws Exception {
        logger.debug("Trying to create channel in server {} (name: {}, voice: {})", ImplServer.this, name, voice);
        JSONObject param = new JSONObject().put("name", name).put("type", voice ? "voice" : "text");
        HttpResponse<JsonNode> response = Unirest.post("https://discordapp.com/api/guilds/" + id + "/channels")
                .header("authorization", api.getToken())
                .header("Content-Type", "application/json")
                .body(param.toString())
                .asJson();
        api.checkResponse(response);
        if (voice) {
            return new ImplVoiceChannel(response.getBody().getObject(), this, api);
        } else {
            return new ImplChannel(response.getBody().getObject(), this, api);
        }
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
