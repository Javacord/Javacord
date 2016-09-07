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

import com.mashape.unirest.http.Unirest;
import de.btobastian.javacord.utils.ThreadPool;

/**
 * This class is used to get a new api instance.
 */
public class Javacord {

    /**
     * The current javacord version.
     */
    public static final String VERSION = "2.0.11";

    /**
     * The github url of javacord.
     */
    public static final String GITHUB_URL = "https://github.com/BtoBastian/Javacord";

    /**
     * The user agent used for requests.
     */
    public static final String USER_AGENT = "Javacord DiscordBot (" + GITHUB_URL + ", v" + VERSION + ")";

    static {
        Unirest.setDefaultHeader("User-Agent", USER_AGENT);
    }

    private Javacord() { }

    /**
     * Gets a new instance of DiscordAPI.
     *
     * You can use different instances to connect to more than one account.
     *
     * @return A new instance of DiscordAPI.
     * @see #getApi(String, String)
     */
    public static DiscordAPI getApi() {
        return new ImplDiscordAPI(new ThreadPool());
    }

    /**
     * Gets a new instance of DiscordAPI.
     *
     * You can use different instances to connect to more than one account.
     *
     * @param email The email address which should be used to connect to the account.
     * @param password The password which should be used to connect to the account.
     * @return A new instance of DiscordAPI.
     * @see DiscordAPI#setEmail(String)
     * @see DiscordAPI#setPassword(String)
     */
    public static DiscordAPI getApi(String email, String password) {
        DiscordAPI api = getApi();
        api.setEmail(email);
        api.setPassword(password);
        return api;
    }

    /**
     * Gets a new instance of DiscordAPI.
     *
     * The only way to login to a bot is by using the token.
     *
     * @param token The token which is required to login.
     * @param bot Whether the token is the token of a bot account or a normal account.
     * @return A new instance of DiscordAPI.
     */
    public static DiscordAPI getApi(String token, boolean bot) {
        DiscordAPI api = getApi();
        api.setToken(token, bot);
        return api;
    }

}
