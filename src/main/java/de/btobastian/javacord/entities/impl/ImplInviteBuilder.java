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
import de.btobastian.javacord.entities.InviteBuilder;
import de.btobastian.javacord.exceptions.PermissionsException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The implementation of the invite builder interface.
 */
public class ImplInviteBuilder implements InviteBuilder {

    private final ImplDiscordAPI api;
    private final ImplChannel channel;

    private int maxUses = -1;
    private byte temporary = -1;
    private int maxAge = -1;

    public ImplInviteBuilder(ImplChannel channel, ImplDiscordAPI api) {
        this.channel = channel;
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
    public Future<String> create() {
        return create(null);
    }

    @Override
    public Future<String> create(FutureCallback<String> callback) {
        ListenableFuture<String> future =
                api.getThreadPool().getListeningExecutorService().submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
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
                        HttpResponse<JsonNode> response = Unirest
                                .post("https://discordapp.com/api/channels/" + channel.getId() + "/invites")
                                .header("authorization", api.getToken())
                                .header("Content-Type", "application/json")
                                .body(jsonParam.toString())
                                .asJson();
                        if (response.getStatus() == 403) {
                            throw new PermissionsException("Missing permissions!");
                        }
                        if (response.getStatus() < 200 || response.getStatus() > 299) {
                            throw new Exception("Received http status code " + response.getStatus()
                                    + " with message " + response.getStatusText());
                        }
                        return response.getBody().getObject().getString("code");
                    }
                });
        if (callback != null) {
            Futures.addCallback(future, callback);
        }
        return future;
    }

}
