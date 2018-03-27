package org.javacord.api.exception;

import org.javacord.api.util.internal.DelegateFactory;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;

import java.util.Optional;

/**
 * This exception is always thrown whenever a request to Discord failed.
 */
public class DiscordException extends Exception {

    /**
     * The request. May be <code>null</code> if the exception was thrown before creating a request.
     */
    private final RestRequestInformation request;

    /**
     * The rest request result. May be <code>null</code> if the exception was thrown before sending a request.
     */
    private final RestRequestResponseInformation response;

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     * @param response The information about the response.
     */
    public DiscordException(Exception origin, String message, RestRequestInformation request,
                            RestRequestResponseInformation response) {
        super(message, origin);
        this.request = request;
        this.response = response;

        DelegateFactory.getDiscordExceptionValidator().validateException(this);
    }

    /**
     * Gets information about the request which caused the exception.
     * May be <code>null</code> if the exception was thrown before creating a request.
     *
     * @return Information about the request which caused the exception.
     */
    public Optional<RestRequestInformation> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Gets information about the response which caused the exception.
     * May not be present if the exception was thrown before sending a request.
     *
     * @return Information about the response which caused the exception.
     */
    public Optional<RestRequestResponseInformation> getResponse() {
        return Optional.ofNullable(response);
    }

}
