package org.javacord.core.util.exception;

import org.javacord.api.exception.DiscordException;
import org.javacord.api.util.exception.DiscordExceptionValidator;
import org.javacord.api.util.rest.RestRequestResponseInformation;
import org.javacord.core.util.rest.RestRequestHttpResponseCode;

import java.util.Optional;

/**
 * Validates if discord exceptions are from the correct type.
 */
public class DiscordExceptionValidatorImpl implements DiscordExceptionValidator {

    @Override
    public void validateException(DiscordException exception) throws AssertionError {
        Optional<RestRequestHttpResponseCode> expectedResponseCodeOptional =
                RestRequestHttpResponseCode.fromDiscordExceptionClass(exception.getClass());
        Optional<RestRequestResponseInformation> restRequestResponseInformationOptional = exception.getResponse();

        if (expectedResponseCodeOptional.isPresent()) {
            // if this class or a superclass of it is the expected exception class for an HTTP response code
            RestRequestHttpResponseCode expectedResponseCode = expectedResponseCodeOptional.get();
            if (restRequestResponseInformationOptional.isPresent()) {
                // if there is an actual result but the response code does not match
                if (restRequestResponseInformationOptional.get().getCode() != expectedResponseCode.getCode()) {
                    throw new AssertionError(exception.getClass().getSimpleName()
                            + " should only be thrown on "
                            + expectedResponseCode.getCode()
                            + " HTTP response code (was "
                            + restRequestResponseInformationOptional.get().getCode()
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
            Optional<Integer> responseCodeOptional = restRequestResponseInformationOptional
                    .map(RestRequestResponseInformation::getCode);
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
