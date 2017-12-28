package de.btobastian.javacord.exceptions;

import de.btobastian.javacord.utils.rest.RestRequest;
import de.btobastian.javacord.utils.rest.RestRequestResult;
import okhttp3.Response;

import java.util.Optional;

/**
 * This exception is always thrown when we receive a response status which isn't between 200 and 299
 */
public class DiscordException extends Exception {

    /**
     * The request. May be <code>null</code> if the exception was thrown before creating a request.
     */
    private final RestRequest<?> request;

    /**
     * The rest request result. May be <code>null</code> if the exception was thrown before sending a request.
     */
    private final RestRequestResult restRequestResult;

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public DiscordException(Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(message, origin);
        this.request = request;
        this.restRequestResult = restRequestResult;
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

    /**
     * Gets the rest request result which caused the exception.
     * May not be present if the exception was thrown before sending a request.
     *
     * @return The rest request result which caused the exception.
     */
    public Optional<RestRequestResult> getRestRequestResult() {
        return Optional.ofNullable(restRequestResult);
    }

}