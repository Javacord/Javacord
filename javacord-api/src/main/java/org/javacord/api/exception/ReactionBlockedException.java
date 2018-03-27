package org.javacord.api.exception;

import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;

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
     * @param request The information about the request.
     * @param response The information about the response.
     */
    public ReactionBlockedException(Exception origin, String message, RestRequestInformation request,
                                    RestRequestResponseInformation response) {
        super(origin, message, request, response);
    }

}
