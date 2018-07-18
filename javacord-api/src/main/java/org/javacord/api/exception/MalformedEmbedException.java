package org.javacord.api.exception;

/**
 * Signals that an embed is malformed.
 */
public class MalformedEmbedException extends IllegalStateException {

    /**
     * Constructs an empty {@link MalformedEmbedException} without additional information.
     */
    public MalformedEmbedException() {
        super();
    }

    /**
     * Constructs a normal {@link MalformedEmbedException} containing a short sentence about whats missing in
     * the embed.
     *
     * @param message A short message that explains whats missing in the embed.
     */
    public MalformedEmbedException(String message) {
        super(message);
    }
}
