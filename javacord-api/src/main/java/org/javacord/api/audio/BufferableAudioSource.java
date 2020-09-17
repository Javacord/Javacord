package org.javacord.api.audio;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * A bufferable audio source.
 */
public interface BufferableAudioSource extends AudioSource {

    /**
     * Sets the buffer size of the audio source.
     *
     * <p>The audio source will pre-download audio frames for the specified buffer size to allow for a
     * smooth playback without stuttering.
     *
     * <p>As audio frames always have a fixed {@code 20ms} duration, the buffer size will be rounded down to the closest
     * multiple of {@code 20ms}. E.g. when calling {@code setBufferSize(79, TimeUnit.MILLISECONDS)}, the buffer size
     * will instead be set to {@code 60ms}.
     *
     * <p>Depending on the audio source, it might not be possible to buffer in 20ms accuracy. In this case, it
     * will try to get as close to the actual buffer size as possible.
     *
     * <p>Negative buffer sizes will be interpreted as {@code 0}.
     *
     * @param size The size of the buffer.
     * @param unit A {@code TimeUnit} determining how to interpret the {@code size} parameter.
     *             As audio frames have a fixed size of {@code 20ms}, units bellow {@link TimeUnit#MILLISECONDS} are
     *             not recommended.
     */
    void setBufferSize(long size, TimeUnit unit);

    /**
     * Gets the size of the buffer.
     *
     * @return The buffer size of the audio source.
     */
    Duration getBufferSize();

    /**
     * Gets the current used size of the buffer.
     *
     * <p>Unlike {@link #getBufferSize()}, this method does not return how much can be buffered, but
     * what's currently buffered.
     *
     * <p>Notice, that it's possible, that the used buffer might exceed the allowed buffer size in some cases.
     *
     * @return The used buffer size of the audio source.
     */
    Duration getUsedBufferSize();

}
