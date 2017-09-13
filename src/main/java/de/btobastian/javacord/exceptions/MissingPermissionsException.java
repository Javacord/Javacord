package de.btobastian.javacord.exceptions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import de.btobastian.javacord.utils.rest.RestRequest;

/**
 * When we are not allowed to perform an action.
 */
public class MissingPermissionsException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     * @param response The response which caused the exception.
     * @param request The request.
     */
    public MissingPermissionsException(String message, HttpResponse<JsonNode> response, RestRequest<?> request) {
        super(message, response, request);
    }

}
