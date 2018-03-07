package org.javacord.util.exception;

import okhttp3.Response;
import org.javacord.exception.DiscordException;
import org.javacord.util.rest.RestRequestHttpResponseCode;
import org.javacord.util.rest.RestRequestResult;
import org.javacord.util.rest.impl.ImplRestRequestResponseInformation;

import java.util.Optional;

/**
 * Validates if discord exceptions are from the correct type.
 */
public class DiscordExceptionValidator {

    private DiscordExceptionValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Validates that the exception is used for the HTTP response code it is meant for and vice versa.
     *
     * @param exception The exception to check.
     * @throws AssertionError If the exception is invalid.
     */
    public static void validateException(DiscordException exception) throws AssertionError {
        Optional<RestRequestHttpResponseCode> expectedResponseCodeOptional =
                RestRequestHttpResponseCode.fromDiscordExceptionClass(exception.getClass());
        Optional<RestRequestResult> restRequestResultOptional = exception.getResponse()
                .map(response -> ((ImplRestRequestResponseInformation) response))
                .map(ImplRestRequestResponseInformation::getRestRequestResult);

        if (expectedResponseCodeOptional.isPresent()) {
            // if this class or a superclass of it is the expected exception class for an HTTP response code
            RestRequestHttpResponseCode expectedResponseCode = expectedResponseCodeOptional.get();
            if (restRequestResultOptional.isPresent()) {
                // if there is an actual result but the response code does not match
                if (restRequestResultOptional.get().getResponse().code() != expectedResponseCode.getCode()) {
                    throw new AssertionError(exception.getClass().getSimpleName()
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
                throw new AssertionError(exception.getClass().getSimpleName()
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

}
