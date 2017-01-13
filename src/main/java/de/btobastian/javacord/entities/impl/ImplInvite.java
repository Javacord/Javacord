/*
 * Copyright (C) 2017 Bastian Oppermann
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
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.*;
import de.btobastian.javacord.utils.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Future;

/**
 * The implementation of the invite interface.
 */
public class ImplInvite implements Invite {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplInvite.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final SimpleDateFormat FORMAT_ALTERNATIVE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat FORMAT_ALTERNATIVE_TWO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    private final ImplDiscordAPI api;

    private final String code;
    private final String serverId;
    private final String serverName;
    private final String channelId;
    private final String channelName;
    private final boolean voice;
    private int maxAge = -1;
    private boolean revoked = false;
    private Calendar creationDate = null;
    private int uses = -1;
    private int maxUses = -1;
    private boolean temporary = false;
    private User creator = null;

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     * @param data A JSONObject containing all necessary data.
     */
    public ImplInvite(ImplDiscordAPI api, JSONObject data) {
        this.api = api;
        this.code = data.getString("code");
        this.serverId = data.getJSONObject("guild").getString("id");
        this.serverName = data.getJSONObject("guild").getString("name");
        this.channelId = data.getJSONObject("channel").getString("id");
        this.channelName = data.getJSONObject("channel").getString("name");
        this.voice = !data.getJSONObject("channel").getString("type").equals("text");
        if (data.has("max_age")) {
            this.maxAge = data.getInt("max_age");
        }
        if (data.has("revoked")) {
            this.revoked = data.getBoolean("revoked");
        }
        if (data.has("created_at")) {
            String time = data.getString("created_at");
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
                            logger.warn("Could not parse timestamp {}. Please contact the developer!", time, e);
                        }
                    }
                }
            }
            creationDate = calendar;
        }
        if (data.has("temporary")) {
            this.temporary = data.getBoolean("temporary");
        }
        if (data.has("uses")) {
            this.uses = data.getInt("uses");
        }
        if (data.has("max_uses")) {
            this.maxUses = data.getInt("max_uses");
            if (this.maxUses == 0) {
                this.maxUses = -1;
            }
        }
        if (data.has("inviter")) {
            this.creator = api.getOrCreateUser(data.getJSONObject("inviter"));
        }
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public URL getInviteUrl() {
        try {
            return new URL("https://discord.gg/" + code);
        } catch (MalformedURLException e) {
            logger.warn("Malformed invite url of invite code {}! Please contact the developer!", code, e);
            return null;
        }
    }

    @Override
    public String getServerId() {
        return serverId;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public Server getServer() {
        return api.getServerById(serverId);
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public String getChannelName() {
        return channelName;
    }

    @Override
    public Channel getChannel() {
        Server server = getServer();
        return server == null ? null : server.getChannelById(channelId);
    }

    @Override
    public VoiceChannel getVoiceChannel() {
        Server server = getServer();
        return server == null ? null : server.getVoiceChannelById(channelId);
    }

    @Override
    public boolean isVoiceChannel() {
        return voice;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public boolean isRevoked() {
        return revoked;
    }

    @Override
    public Calendar getCreationDate() {
        if (creationDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate.getTime());
        return calendar;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public boolean isTemporary() {
        return temporary;
    }

    @Override
    public User getCreator() {
        return creator;
    }

    @Override
    public Future<Server> acceptInvite() {
        return acceptInvite(null);
    }

    @Override
    public Future<Server> acceptInvite(FutureCallback<Server> callback) {
        return api.acceptInvite(getCode(), callback);
    }

    @Override
    public Future<Void> delete() {
        return api.deleteInvite(getCode());
    }

    @Override
    public String toString() {
        return getCode();
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }

}
