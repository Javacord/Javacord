package org.javacord.api.exception;

import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;

/**
 * When the user of the connected account cannot message a user.
 * This might have several reasons, e.g.
 * <ul>
 *   <li>The user blocked you</li>
 *   <li>You don't share a server with the user</li>
 *   <li>The user does not allow messages from the users in the servers you share with it</li>
 * </ul>
 */
public class CannotMessageUserException extends MissingPermissionsException {
    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     * @param response The information about the response.
     */
    public CannotMessageUserException(Exception origin, String message, RestRequestInformation request,
                                      RestRequestResponseInformation response) {
        super(origin, message, request, response);
    }

}
