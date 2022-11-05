package org.javacord.api.exception;

/**
 * When a method requires a specific {@link org.javacord.api.entity.intent.Intent} to be usable.
 */
public class MissingIntentException extends RuntimeException {

    /**
     * Creates a new instance of this class.
     *
     * @param message The message of the exception.
     */
    public MissingIntentException(String message) {
        super(message);
    }

}
