package de.btobastian.javacord.exceptions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

/**
 * This exception is always thrown when we receive a response status which isn't between 200 and 299
 */
public class BadResponseException extends Exception {

    private final HttpResponse<JsonNode> response;

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     * @param response The response which caused the exception.
     */
    public BadResponseException(String message, HttpResponse<JsonNode> response) {
        super(message);
        this.response = response;
    }

    /**
     * Gets the status of the response.
     *
     * @return The status of the response.
     */
    public int getStatus() {
        return response.getStatus();
    }

    /**
     * Gets the status text of the response.
     *
     * @return The status text of the response.
     */
    public String getStatusText() {
        return response.getStatusText();
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