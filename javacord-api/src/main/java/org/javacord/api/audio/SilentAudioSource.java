package org.javacord.api.audio;

import org.javacord.api.DiscordApi;

import java.util.concurrent.TimeUnit;

/**
 * A silent audio source that's always muted.
 *
 * <p>Can be used to create "gaps" between audio sources.
 */
public class SilentAudioSource extends AudioSourceBase implements PauseableAudioSource {

    /**
     * A frame of silence.
     */
    public static final byte[] SILENCE_FRAME = {(byte) 0xF8, (byte) 0xFF, (byte) 0xFE};

    private final long initialDuration;
    private long duration;
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
        initialDuration = unit.toMillis(duration) / 20;
        this.duration = initialDuration;
    }

    @Override
    public byte[] getNextFrame() {
        return null;
    }

    @Override
    public boolean hasNextFrame() {
        if (paused) {
            // Don't decrement the duration if paused
            return false;
        }
        duration--;
        return false;
    }

    @Override
    public boolean hasFinished() {
        return duration <= 0;
    }

    @Override
    public boolean isMuted() {
        return true;
    }

    @Override
    public AudioSource clone() {
        return new SilentAudioSource(getApi(), initialDuration * 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }
}
