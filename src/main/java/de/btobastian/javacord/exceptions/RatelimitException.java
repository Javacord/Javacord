package de.btobastian.javacord.exceptions;

/**
 * When we encounter a rate limit and ran out of retires.
 */
public class RatelimitException extends Exception {

    /**
     * Creates a new RatelimitException.
     *
     * @param message The message of the exception.
     */
    public RatelimitException(String message) {
        super(message);
    }

}