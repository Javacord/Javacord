package org.javacord.exception;

import okhttp3.Response;
import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestHttpResponseCode;
import org.javacord.util.rest.RestRequestResult;

import java.util.Optional;

/**
 * This exception is always thrown when we receive a response status which isn't between 200 and 299
 */
public class DiscordException extends Exception {

    /**
     * The request. May be <code>null</code> if the exception was thrown before creating a request.
     */
    private final RestRequest<?> request;

    /**
     * The rest request result. May be <code>null</code> if the exception was thrown before sending a request.
     */
    private final RestRequestResult restRequestResult;

    /**
     * Creates a new instance of this class.
     *
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The request.
     * @param restRequestResult The rest request result which caused the exception.
     */
    public DiscordException(
            Exception origin, String message, RestRequest<?> request, RestRequestResult restRequestResult) {
        super(message, origin);
        this.request = request;
        this.restRequestResult = restRequestResult;

        // make sure the exceptions are used for the HTTP response codes they are meant for and vice versa
        Optional<RestRequestHttpResponseCode> expectedResponseCodeOptional =
                RestRequestHttpResponseCode.fromDiscordExceptionClass(getClass());
        Optional<RestRequestResult> restRequestResultOptional = getRestRequestResult();

        if (expectedResponseCodeOptional.isPresent()) {
            // if this class or a superclass of it is the expected exception class for an HTTP response code
            RestRequestHttpResponseCode expectedResponseCode = expectedResponseCodeOptional.get();
            if (restRequestResultOptional.isPresent()) {
                // if there is an actual result but the response code does not match
                if (restRequestResultOptional.get().getResponse().code() != expectedResponseCode.getCode()) {
                    throw new AssertionError(getClass().getSimpleName()
                                             + " should only be thrown on "
                                             + expectedResponseCode.getCode()
                                             + " HTTP response code (was "
                                             + restRequestResultOptional.get().getResponse().code()
                                             + ") or it should not be a subclass of "
                                             + expectedResponseCode
                                                     .getDiscordExceptionClass()
                                                     .orElseThrow(AssertionError::new)
                                                     .getSimpleName()
                                             + ". Please contact the developer!");
                }
            } else {
                // if there is no actual result
                throw new AssertionError(getClass().getSimpleName()
                                         + " should only be thrown on "
                                         + expectedResponseCode.getCode()
                                         + " HTTP response code but there is no result. "
                                         + "Please contact the developer!");
            }
        } else {
            // if this class or a superclass of it is not the expected exception class for an HTTP response code
            Optional<Integer> responseCodeOptional = restRequestResultOptional
                    .map(RestRequestResult::getResponse)
                    .map(Response::code);
            Optional<? extends Class<? extends DiscordException>> discordExceptionClassOptional =
                    responseCodeOptional
                            .flatMap(RestRequestHttpResponseCode::fromCode)
                            .flatMap(RestRequestHttpResponseCode::getDiscordExceptionClass);
            // if there is a result present and for its HTTP response code exists an expected exception
            if (discordExceptionClassOptional.isPresent()) {
                throw new AssertionError("For "
                                         + responseCodeOptional.orElseThrow(AssertionError::new)
                                         + " HTTP response code an exception of type "
                                         + discordExceptionClassOptional.get().getSimpleName()
                                         + " or a sub-class thereof should be thrown. "
                                         + "Please contact the developer!");
            }
        }
    }

    /**
     * Gets the request which caused the exception.
     * May be <code>null</code> if the exception was thrown before creating a request.
     *
     * @return The request which caused the exception.
     */
    public Optional<RestRequest<?>> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Gets the rest request result which caused the exception.
     * May not be present if the exception was thrown before sending a request.
     *
     * @return The rest request result which caused the exception.
     */
    public Optional<RestRequestResult> getRestRequestResult() {
        return Optional.ofNullable(restRequestResult);
    }

}