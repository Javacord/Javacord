package org.javacord.exception;

import org.javacord.util.rest.RestRequestInformation;
import org.javacord.util.rest.RestRequestResponseInformation;

/**
 * When we are not allowed to perform an action (HTTP response code 403).
 */
public class MissingPermissionsException extends DiscordException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
     * @param response The information about the response.
     */
    public MissingPermissionsException(Exception origin, String message, RestRequestInformation request,
                                       RestRequestResponseInformation response) {
        super(origin, message, request, response);
    }

}
