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
import de.btobastian.javacord.entities.Invite;
import de.btobastian.javacord.entities.InviteBuilder;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RateLimitType;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The implementation of the invite builder interface.
 */
public class ImplInviteBuilder implements InviteBuilder {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplInviteBuilder.class);

    private final ImplDiscordAPI api;
    private final ImplChannel textChannel;
    private final ImplVoiceChannel voiceChannel;

    private int maxUses = -1;
    private byte temporary = -1;
    private int maxAge = -1;

    public ImplInviteBuilder(ImplChannel textChannel, ImplDiscordAPI api) {
        this.textChannel = textChannel;
        this.voiceChannel = null;
        this.api = api;
    }

    public ImplInviteBuilder(ImplVoiceChannel voiceChannel, ImplDiscordAPI api) {
        this.textChannel = null;
        this.voiceChannel = voiceChannel;
        this.api = api;
    }

    @Override
    public InviteBuilder setMaxUses(int maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    @Override
    public InviteBuilder setTemporary(boolean temporary) {
        this.temporary = temporary ? (byte) 1 : 0;
        return this;
    }

    @Override
    public InviteBuilder setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    @Override
    public Future<Invite> create() {
        return create(null);
    }

    @Override
    public Future<Invite> create(FutureCallback<Invite> callback) {
        ListenableFuture<Invite> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<Invite>() {
                    @Override
                    public Invite call() throws Exception {
                        logger.debug("Trying to create invite for channel {} (max uses: {}, temporary: {}, max age: {}",
                                textChannel == null ? voiceChannel : textChannel, maxUses, temporary, maxAge);
                        JSONObject jsonParam = new JSONObject();
                        if (maxUses > 0) {
                            jsonParam.put("max_uses", maxUses);
                        }
                        if (temporary > -1) {
                            jsonParam.put("temporary", temporary == 1);
                        }
                        if (maxAge > 0) {
                            jsonParam.put("max_age", maxAge);
                        }
                        String channelId = textChannel == null ? voiceChannel.getId() : textChannel.getId();
                        HttpResponse<JsonNode> response = Unirest
                                .post("https://discordapp.com/api/channels/" + channelId + "/invites")
                                .header("authorization", api.getToken())
                                .header("Content-Type", "application/json")
                                .body(jsonParam.toString())
                                .asJson();
                        api.checkResponse(response);
                        api.checkRateLimit(response, RateLimitType.UNKNOWN, null);
                        JSONObject data = response.getBody().getObject();
                        logger.debug("Created invite for channel {} (max uses: {}, temporary: {}, max age: {}",
                                textChannel == null ? voiceChannel : textChannel, maxUses, temporary,
                                data.has("max_age") ? data.getInt("max_age") : -1);
                        return new ImplInvite(api, data);
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

}
