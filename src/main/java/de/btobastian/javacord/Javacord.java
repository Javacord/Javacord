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
package de.btobastian.javacord;

import com.mashape.unirest.http.Unirest;

/**
 * This class contains some static information about Javacord.
 */
public class Javacord {

    /**
     * The current javacord version.
     */
    public static final String VERSION = "3.0.0";

    /**
     * The github url of javacord.
     */
    public static final String GITHUB_URL = "https://github.com/BtoBastian/Javacord";

    /**
     * The user agent used for requests.
     */
    public static final String USER_AGENT = "DiscordBot (" + GITHUB_URL + ", v" + VERSION + ")";

    /**
     * The gateway protocol version from Discord which we are using.
     * A list with all protocol versions can be found
     * <a href="https://discordapp.com/developers/docs/topics/gateway#gateway-protocol-versions">here</a>.
     */
    public static final String DISCORD_GATEWAY_PROTOCOL_VERSION = "6";

    static {
        Unirest.setDefaultHeader("User-Agent", USER_AGENT);
    }

    private Javacord() { }

}
