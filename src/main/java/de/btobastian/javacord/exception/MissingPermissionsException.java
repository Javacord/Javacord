package de.btobastian.javacord.exception;

import de.btobastian.javacord.util.rest.RestRequest;
import de.btobastian.javacord.util.rest.RestRequestResult;

/**
 * When we are not allowed to perform an action (HTTP response code 403).
 */
public class MissingPermissionsException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public MissingPermissionsException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(origin, message, request, restRequestResult);
    }

}
