package de.btobastian.javacord.exceptions;

import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.Response;

import java.util.Optional;

/**
 * This exception is always thrown when we receive a response status which isn't between 200 and 299
 */
public class DiscordException extends Exception {

    /**
     * The response. May be <code>null</code> if the exception was thrown before sending a request.
     */
    private final Response response;

    /**
     * The request. May be <code>null</code> if the exception was thrown before creating a request.
     */
    private final RestRequest<?> request;

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param response The response which caused the exception.
     * @param request The request.
     */
    public DiscordException(Exception origin, String message, Response response, RestRequest<?> request) {
        super(message, origin);
        this.response = response;
        this.request = request;
    }

    /**
     * Gets the response which caused the exception.
     * May not be present if the exception was thrown before sending a request.
     *
     * @return The response which caused the exception.
     */
    public Optional<Response> getResponse() {
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