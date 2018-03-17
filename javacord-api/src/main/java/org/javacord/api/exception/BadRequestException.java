package org.javacord.api.exception;

import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;

/**
 * When we sent a bad request (HTTP response code 400).
 */
public class BadRequestException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     * @param response The information about the response.
     */
    public BadRequestException(Exception origin, String message, RestRequestInformation request,
                               RestRequestResponseInformation response) {
        super(origin, message, request, response);
    }

}
