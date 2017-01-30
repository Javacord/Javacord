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
package de.btobastian.javacord.exceptions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

/**
 * This exception is always thrown when we receive a response status which isn't between 200 and 299
 */
public class BadResponseException extends Exception {

    private final int status;
    private final String statusText;
    private final HttpResponse<JsonNode> response;

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     * @param status The status of the response.
     * @param statusText The status text of the response.
     * @param response The response which caused the exception.
     */
    public BadResponseException(String message, int status, String statusText, HttpResponse<JsonNode> response) {
        super(message);
        this.status = status;
        this.statusText = statusText;
        this.response = response;
    }

    /**
     * Gets the status of the response.
     *
     * @return The status of the response.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets the status text of the response.
     *
     * @return The status text of the response.
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Gets the response which caused the exception.
     *
     * @return The response which caused the exception.
     */
    public HttpResponse<JsonNode> getResponse() {
        return response;
    }

}
