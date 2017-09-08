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
package de.btobastian.javacord.utils;

import de.btobastian.javacord.Javacord;

/**
 * This enum contains all endpoints which we may use.
 */
public enum RestEndpoints {

    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot");

    /**
     * The endpoint url (only including the base, not the https://discordapp.com/api/vXYZ/ "prefix".
     */
    private final String endpointUrl;

    RestEndpoints(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    /**
     * Gets the endpoint url (only including the base, not the https://discordapp.com/api/vXYZ/ "prefix".
     *
     * @return The gateway url.
     */
    public String getEndpointUrl() {
        return endpointUrl;
    }

    /**
     * Gets the full url of the endpoint.
     *
     * @param parameters The parameters of the url. E.g. for channel ids.
     * @return The full url of the endpoint.
     */
    public String getFullUrl(Object... parameters) {
        String url = "https://discordapp.com/api/v" + Javacord.DISCORD_GATEWAY_PROTOCOL_VERSION + getEndpointUrl();
        return String.format(url, parameters);
    }

}
