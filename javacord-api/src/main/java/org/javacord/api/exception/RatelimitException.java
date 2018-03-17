package org.javacord.api.exception;

import org.javacord.api.util.rest.RestRequestInformation;

/**
 * When we encounter a rate limit and ran out of retires.
 */
public class RatelimitException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     */
    public RatelimitException(Exception origin, String message, RestRequestInformation request) {
        super(origin, message, request, null);
    }

}