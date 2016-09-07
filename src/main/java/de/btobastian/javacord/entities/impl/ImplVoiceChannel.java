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
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.InviteBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.listener.voicechannel.VoiceChannelChangeNameListener;
import de.btobastian.javacord.listener.voicechannel.VoiceChannelDeleteListener;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * The implementation of the voice channel interface.
 */
public class ImplVoiceChannel implements VoiceChannel {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplVoiceChannel.class);

    private static final Permissions emptyPermissions = new ImplPermissions(0, 0);

    private final ImplDiscordAPI api;

    private final String id;
    private String name;
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
    public ImplVoiceChannel(JSONObject data, ImplServer server, ImplDiscordAPI api) {
        this.api = api;
        this.server = server;

        id = data.getString("id");
        name = data.getString("name");
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

        server.addVoiceChannel(this);
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
                logger.debug("Trying to delete voice channel {}", ImplVoiceChannel.this);
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .delete("https://discordapp.com/api/channels/" + id)
                            .header("authorization", api.getToken())
                            .asJson();
                    api.checkResponse(response);
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, server);
                    server.removeVoiceChannel(ImplVoiceChannel.this);
                    logger.info("Deleted voice channel {}", ImplVoiceChannel.this);
                    // call listener
                    api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                        @Override
                        public void run() {
                            List<VoiceChannelDeleteListener> listeners =
                                    api.getListeners(VoiceChannelDeleteListener.class);
                            synchronized (listeners) {
                                for (VoiceChannelDeleteListener listener : listeners) {
                                    try {
                                        listener.onVoiceChannelDelete(api, ImplVoiceChannel.this);
                                    } catch (Throwable t) {
                                        logger.warn("Uncaught exception in VoiceChannelDeleteListener!", t);
                                    }
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
    public InviteBuilder getInviteBuilder() {
        return new ImplInviteBuilder(this, api);
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
    public Future<Exception> updateName(final String newName) {
        final JSONObject params = new JSONObject()
                .put("name", newName);
        return api.getThreadPool().getExecutorService().submit(new Callable<Exception>() {
            @Override
            public Exception call() throws Exception {
                logger.debug("Trying to update voice channel {} (new name: {}, old name: {})",
                        ImplVoiceChannel.this, newName, getName());
                try {
                    HttpResponse<JsonNode> response = Unirest
                            .patch("https://discordapp.com/api/channels/" + getId())
                            .header("authorization", api.getToken())
                            .header("Content-Type", "application/json")
                            .body(params.toString())
                            .asJson();
                    api.checkResponse(response);
                    api.checkRateLimit(response, RateLimitType.UNKNOWN, server);
                    String updatedName = response.getBody().getObject().getString("name");
                    logger.debug("Updated voice channel {} (new name: {}, old name: {})",
                            ImplVoiceChannel.this, updatedName, getName());
                    // check name
                    if (!updatedName.equals(getName())) {
                        final String oldName = getName();
                        setName(updatedName);
                        api.getThreadPool().getSingleThreadExecutorService("listeners").submit(new Runnable() {
                            @Override
                            public void run() {
                                List<VoiceChannelChangeNameListener> listeners =
                                        api.getListeners(VoiceChannelChangeNameListener.class);
                                synchronized (listeners) {
                                    for (VoiceChannelChangeNameListener listener : listeners) {
                                        try {
                                            listener.onVoiceChannelChangeName(api, ImplVoiceChannel.this, oldName);
                                        } catch (Throwable t) {
                                            logger.warn("Uncaught exception in VocieChannelChangeNameListener!", t);
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

    /**
     * Sets the name of the channel (no update!).
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
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
