package org.javacord.util.exception;

import org.javacord.exception.DiscordException;

/**
 * Validates if discord exceptions are from the correct type.
 */
public interface DiscordExceptionValidator {

    /**
     * Validates that the exception is used for the HTTP response code it is meant for and vice versa.
     *
     * @param exception The exception to check.
     * @throws AssertionError If the exception is invalid.
     */
    void validateException(DiscordException exception) throws AssertionError;

}
