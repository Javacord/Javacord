package org.javacord.api.audio;

/**
 * Used to intercept and transform audio frames.
 *
 * <p>An example use case is adjusting volume.
 */
public interface AudioTransformer {

    /**
     * Intercepts and transforms the incoming audio frame.
     *
     * @param source The audio source the frame is coming from.
     * @param originalFrame The original audio frame.
     * @return The transformed audio frame.
     */
    byte[] transform(AudioSource source, byte[] originalFrame);

}
