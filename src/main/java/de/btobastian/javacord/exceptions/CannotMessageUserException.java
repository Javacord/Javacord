package de.btobastian.javacord.exceptions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import de.btobastian.javacord.utils.rest.RestRequest;

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
     * @param message The message of the exception.
     * @param response The response which caused the exception.
     * @param request The request.
     */
    public CannotMessageUserException(String message, HttpResponse<JsonNode> response, RestRequest<?> request) {
        super(message, response, request);
    }

}
