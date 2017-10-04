package de.btobastian.javacord.exceptions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.Optional;

/**
 * This exception is always thrown when we receive a response status which isn't between 200 and 299
 */
public class DiscordException extends Exception {

    /**
     * The response. May be <code>null</code> if the exception was thrown before sending a request.
     */
    private final HttpResponse<JsonNode> response;

    /**
     * The request. May be <code>null</code> if the exception was thrown before creating a request.
     */
    private final RestRequest<?> request;

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     * @param response The response which caused the exception.
     * @param request The request.
     */
    public DiscordException(String message, HttpResponse<JsonNode> response, RestRequest<?> request) {
        super(message);
        this.response = response;
        this.request = request;
    }

    /**
     * Gets the response which caused the exception.
     * May not be present if the exception was thrown before sending a request.
     *
     * @return The response which caused the exception.
     */
    public Optional<HttpResponse<JsonNode>> getResponse() {
        return Optional.ofNullable(response);
    }

    /**
     * Gets the request which caused the exception.
     * May be <code>null</code> if the exception was thrown before creating a request.
     *
     * @return The request which caused the exception.
     */
    public Optional<RestRequest<?>> getRequest() {
        return Optional.ofNullable(request);
    }

}