package org.javacord.exception;

import org.javacord.util.rest.RestRequest;

/**
 * When we encounter a rate limit and ran out of retires.
 */
public class RatelimitException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     */
    public RatelimitException(Exception origin, String message, RestRequest<?> request) {
        super(origin, message, request, null);
    }

}