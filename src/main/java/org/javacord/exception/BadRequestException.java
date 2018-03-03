package org.javacord.exception;

import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;
import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;

/**
 * When we sent a bad request (HTTP response code 400).
 */
public class BadRequestException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public BadRequestException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(origin, message, request, restRequestResult);
    }

}
