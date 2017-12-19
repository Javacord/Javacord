package de.btobastian.javacord.exceptions;

import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.Response;

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
     * @param response The response which caused the exception.
     * @param request The request.
     */
    public CannotMessageUserException(
            Exception origin, String message, Response response, RestRequest<?> request) {
        super(origin, message, response, request);
    }

}
