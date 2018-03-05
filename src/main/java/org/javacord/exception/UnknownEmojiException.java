package org.javacord.exception;

import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;

/**
 * When the emoji that was sent, for example as reaction, is not an emoji Discord knows about.
 */
public class UnknownEmojiException extends BadRequestException {

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public UnknownEmojiException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(origin, message, request, restRequestResult);
    }

}
