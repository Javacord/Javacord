package org.javacord.api.audio;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * A seekable audio source.
 */
public interface SeekableAudioSource extends AudioSource {

    /**
     * Sets the position of the audio source.
     *
     * <p>As audio frames always have a fixed {@code 20ms} duration, the position will be rounded down to the closest
     * multiple of {@code 20ms}. E.g. when calling {@code setPosition(79, TimeUnit.MILLISECONDS)}, the position will
     * instead be set to {@code 60ms}.
     *
     * <p>Attempting to set the position to a value that's larger than the audio source's duration, will instead set it
     * to the maximum duration of the audio source.
     *
     * <p>Negative positions will be interpreted as {@code 0}.
     *
     * @param position The position to jump to.
     * @param unit A {@code TimeUnit} determining how to interpret the {@code position} parameter.
     *             As audio frames have a fixed size of {@code 20ms}, units bellow {@link TimeUnit#MILLISECONDS} are
     *             not recommended.
     * @return The new position of the audio source in the given time unit.
     */
    long setPosition(long position, TimeUnit unit);

    /**
     * Gets the position of the audio source.
     *
     * @return The position of the audio source.
     */
    Duration getPosition();

    /**
     * Gets the duration of the audio source.
     *
     * @return The duration of the audio source.
     */
    Duration getDuration();
}
