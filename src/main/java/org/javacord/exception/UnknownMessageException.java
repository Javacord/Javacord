package org.javacord.exception;

import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;
import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;

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
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public UnknownMessageException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(origin, message, request, restRequestResult);
    }

}
