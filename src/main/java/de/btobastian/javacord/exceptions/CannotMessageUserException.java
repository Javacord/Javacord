package de.btobastian.javacord.exceptions;

import de.btobastian.javacord.utils.rest.RestRequest;
import de.btobastian.javacord.utils.rest.RestRequestResult;

/**
 * When the bot cannot message a user.
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
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public CannotMessageUserException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(origin, message, request, restRequestResult);
    }

}
