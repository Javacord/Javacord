package org.javacord.exception;

import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;

/**
 * When the user of the connected account cannot add a reaction to a message a user has written.
 * This might happen when the user blocked you.
 */
public class ReactionBlockedException extends MissingPermissionsException {
    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public ReactionBlockedException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(origin, message, request, restRequestResult);
    }

}
