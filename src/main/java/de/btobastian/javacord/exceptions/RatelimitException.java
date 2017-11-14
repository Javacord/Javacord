package de.btobastian.javacord.exceptions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import de.btobastian.javacord.utils.rest.RestRequest;

/**
 * When we encounter a rate limit and ran out of retires.
 */
public class RatelimitException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param response The response which caused the exception.
     * @param request The request.
     */
    public RatelimitException(
            Exception origin, String message, HttpResponse<JsonNode> response, RestRequest<?> request) {
        super(origin, message, response, request);
    }

}