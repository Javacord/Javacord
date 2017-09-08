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
package de.btobastian.javacord.utils.rest;

import de.btobastian.javacord.Javacord;

import java.util.Optional;

/**
 * This enum contains all endpoints which we may use.
 */
public enum RestEndpoint {

    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot");

    /**
     * The endpoint url (only including the base, not the https://discordapp.com/api/vXYZ/ "prefix".
     */
    private final String endpointUrl;

    /**
     * The position of the major parameter starting with <code>0</code> or <code>-1</code> if no major parameter exists.
     */
    private final int majorParameterPosition;

    /**
     * Whether the endpoint is global or not.
     */
    private boolean global;

    RestEndpoint(String endpointUrl) {
        this(endpointUrl, -1, false);
    }

    RestEndpoint(String endpointUrl, boolean global) {
        this(endpointUrl, -1, global);
    }

    RestEndpoint(String endpointUrl, int majorParameterPosition) {
        this(endpointUrl, majorParameterPosition, false);
    }

    RestEndpoint(String endpointUrl, int majorParameterPosition, boolean global) {
        this.endpointUrl = endpointUrl;
        this.majorParameterPosition = majorParameterPosition;
        this.global = global;
    }

    /**
     * Gets the major parameter position of the endpoint.
     * If an endpoint has a major parameter, it means that the ratelimits for this endpoint are based on this parameter.
     * The position starts counting at <code>0</code>!
     *
     * @return An optional which is present, if the endpoint has a major parameter.
     */
    public Optional<Integer> getMajorParameterPosition() {
        if (majorParameterPosition >= 0) {
            return Optional.of(majorParameterPosition);
        }
        return Optional.empty();
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
     * Checks if the endpoint is global.
     *
     * @return Whether the endpoint is global or not.
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Sets whether this endpoint is global or not.
     *
     * @param global If the endpoint is global.
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * Gets the full url of the endpoint.
     *
     * @param parameters The parameters of the url. E.g. for channel ids.
     * @return The full url of the endpoint.
     */
    public String getFullUrl(String... parameters) {
        String url = "https://discordapp.com/api/v" + Javacord.DISCORD_GATEWAY_PROTOCOL_VERSION + getEndpointUrl();
        return String.format(url, (Object[]) parameters);
    }

}
