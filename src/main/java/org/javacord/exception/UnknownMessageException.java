package org.javacord.exception;

import org.javacord.util.rest.RestRequestInformation;
import org.javacord.util.rest.RestRequestResponseInformation;

/**
 * When the message that was referenced, for example for adding a reaction to, is not a message Discord knows about
 * (anymore).
 */
public class UnknownMessageException extends NotFoundException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     * @param response The information about the response.
     */
    public UnknownMessageException(Exception origin, String message, RestRequestInformation request,
                                   RestRequestResponseInformation response) {
        super(origin, message, request, response);
    }

}
