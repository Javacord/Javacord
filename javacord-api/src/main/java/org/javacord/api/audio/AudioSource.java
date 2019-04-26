package org.javacord.api.audio;

import org.javacord.api.DiscordApi;
import org.javacord.api.listener.audio.AudioSourceAttachableListenerManager;
import org.javacord.api.util.Specializable;

import java.util.List;
import java.util.Optional;

public interface AudioSource extends AudioSourceAttachableListenerManager, Specializable<AudioSource> {

    /**
     * Gets the discord api instance for this audio source.
     *
     * @return The discord api instance.
     */
    DiscordApi getApi();

    /**
     * Adds a transformer to the audio source.
     *
     * @param transformer The transformer.
     */
    void addTransformer(AudioTransformer transformer);

    /**
     * Removes a transformer from the audio source.
     *
     * @param transformer The transformer.
     * @return If the audio source contained the specified transformer.
     */
    boolean removeTransformer(AudioTransformer transformer);

    /**
     * Gets a list of all transformers of this audio source.
     *
     * @return A list with all transformers.
     */
    List<AudioTransformer> getTransformers();

    /**
     * Removes all transformers from the audio source.
     */
    void removeTransformers();

    /**
     * Polls for the next 20ms of audio from the source.
     *
     * @return A byte array containing 20ms of audio, or null if {@link #hasNextFrame()} is false.
     */
    byte[] getNextFrame();

    /**
     * Checks whether there is 20ms of audio available to be polled.
     *
     * <p>If there is no frame available, but the source has not been finished, it will
     * play a silent sound instead.
     *
     * @return Whether or not there is a frame available to be polled.
     */
    boolean hasNextFrame();

    /**
     * Checks whether the audio source has finished and can be dequeued.
     *
     * <p>This should not be confused with {@link #hasNextFrame()} which only indicated if there is a
     * frame available right now. An audio source might have no frame available, but is still not
     * finished, e.g. because it's streaming something but downloads it too slowly.
     *
     * @return Whether the audio source has finished and can be dequeued.
     */
    boolean hasFinished();

    /**
     * Mutes the audio source.
     *
     * <p>Equivalent to calling {@code setMuted(true)}.
     *
     * @see #setMuted(boolean)
     */
    default void mute() {
        setMuted(true);
    }

    /**
     * Unmutes the audio source.
     *
     * <p>Equivalent to calling {@code setMuted(false)}.
     *
     * @see #setMuted(boolean)
     */
    default void unmute() {
        setMuted(false);
    }

    /**
     * Sets whether the audio source should be muted.
     *
     * <p>A muted audio source will still continue.
     * This means, that after unmuting the audio source will be at a different "position".
     *
     * @param muted Whether the audio source should be muted.
     */
    void setMuted(boolean muted);

    /**
     * Checks whether the audio source is muted.
     *
     * @return Whether the audio source is muted.
     */
    boolean isMuted();

    /**
     * Creates a copy of the audio source that can be reused for another audio connection.
     *
     * <p>Does not copy the state of the audio source, e.g. if it is muted, it's transformers, etc.
     *
     * @return A copy of the audio source.
     */
    AudioSource copy();

    /**
     * Gets this audio source as a {@code PauseableAudioSource}.
     *
     * @return This audio source as {@code PauseableAudioSource}.
     */
    default Optional<PauseableAudioSource> asPauseableAudioSource() {
        return as(PauseableAudioSource.class);
    }

    /**
     * Gets this audio source as a {@code DownloadableAudioSource}.
     *
     * @return This audio source as {@code DownloadableAudioSource}.
     */
    default Optional<DownloadableAudioSource> asDownloadableAudioSource() {
        return as(DownloadableAudioSource.class);
    }

    /**
     * Gets this audio source as a {@code BufferableAudioSource}.
     *
     * @return This audio source as {@code BufferableAudioSource}.
     */
    default Optional<BufferableAudioSource> asBufferableAudioSource() {
        return as(BufferableAudioSource.class);
    }

    /**
     * Gets this audio source as a {@code SeekableAudioSource}.
     *
     * @return This audio source as {@code SeekableAudioSource}.
     */
    default Optional<SeekableAudioSource> asSeekableAudioSource() {
        return as(SeekableAudioSource.class);
    }
}
