package org.javacord.api.audio;

import org.javacord.api.DiscordApi;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A silent audio source that's always muted.
 *
 * <p>Can be used to create "gaps" between audio sources.
 */
public class SilentAudioSource extends AudioSourceBase implements PauseableAudioSource, SeekableAudioSource {

    /**
     * A frame of silence.
     */
    public static final byte[] SILENCE_FRAME = {(byte) 0xF8, (byte) 0xFF, (byte) 0xFE};

    private final long duration;
    private final AtomicLong position;
    private volatile boolean paused = false;

    /**
     * Creates a new silent audio source.
     *
     * <p>As audio frames always have a fixed {@code 20ms} duration, the duration will be rounded down to the closest
     * multiple of {@code 20ms}. E.g. when trying to set the duration to {@code 79ms}, the duration will instead be
     * set to {@code 60ms}.
     *
     * @param api The discord api instance.
     * @param duration How long it should be silent.
     * @param unit A {@code TimeUnit} determining how to interpret the {@code duration} parameter.
     *             As audio frames have a fixed size of {@code 20ms}, units bellow {@link TimeUnit#MILLISECONDS} are
     *             not recommended.
     */
    public SilentAudioSource(DiscordApi api, long duration, TimeUnit unit) {
        super(api);
        this.duration = unit.toMillis(duration) / 20;
        position = new AtomicLong(this.duration);
    }

    /**
     * Creates a new silent audio source and copies the duration settings from the given one.
     *
     * <p>Does not copy the state of the audio source, e.g. if it is muted, it's transformers, etc.
     *
     * @param toCopy The silent audio source to copy from.
     */
    public SilentAudioSource(SilentAudioSource toCopy) {
        this(toCopy.getApi(), toCopy.duration * 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public byte[] getNextFrame() {
        return applyTransformers(null);
    }

    @Override
    public boolean hasNextFrame() {
        if (paused) {
            // Don't increment the position if paused
            return false;
        }
        position.getAndIncrement();
        return false;
    }

    @Override
    public boolean hasFinished() {
        return position.get() >= duration;
    }

    @Override
    public boolean isMuted() {
        return true;
    }

    @Override
    public AudioSource copy() {
        return new SilentAudioSource(this);
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public long setPosition(long position, TimeUnit unit) {
        long newPosition = unit.toMillis(position) / 20;

        // Don't set a position that's larger than the duration
        if (newPosition >= duration) {
            newPosition = duration;
        }
        this.position.set(newPosition);

        return unit.convert(newPosition * 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public Duration getPosition() {
        return Duration.ofMillis(position.get() * 20);
    }

    @Override
    public Duration getDuration() {
        return Duration.ofMillis(duration * 20);
    }
}
