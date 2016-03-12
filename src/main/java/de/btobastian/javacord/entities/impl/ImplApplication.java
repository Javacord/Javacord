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

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Application;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.utils.LoggerUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.concurrent.Future;

/**
 * The implementation of the Application interface.
 */
public class ImplApplication implements Application {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplApplication.class);

    private final ImplDiscordAPI api;

    private final String id;
    private final String description;
    private final String[] redirectUris;
    private final String name;
    private final String secret;
    private final String botToken;
    private final User bot;

    /**
     * Creates a new instance of this class.
     *
     * @param api The api of the application.
     * @param data The data of the application.
     */
    public ImplApplication(ImplDiscordAPI api, JSONObject data) {
        this.api = api;

        id = data.getString("id");
        description = data.getString("description");
        JSONArray jsonRedirectUris = data.getJSONArray("redirect_uris");
        redirectUris = new String[jsonRedirectUris.length()];
        for (int i = 0; i < redirectUris.length; i++) {
            redirectUris[i] = jsonRedirectUris.getString(i);
        }
        name = data.getString("name");
        secret = data.getString("secret");
        if (data.has("bot")) {
            botToken = data.getJSONObject("bot").getString("token");
            bot = api.getOrCreateUser(data.getJSONObject("bot"));
        } else {
            botToken = null;
            bot = null;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getRedirectUris() {
        return redirectUris;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public User getBot() {
        return bot;
    }

    @Override
    public Future<Exception> delete() {
        return api.deleteApplication(getId());
    }

    @Override
    public String toString() {
        return getName() + " (id: " + getId() + ")";
    }
}
