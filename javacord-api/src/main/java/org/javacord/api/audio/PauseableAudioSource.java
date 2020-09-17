package org.javacord.api.audio;

/**
 * A pauseable audio source.
 */
public interface PauseableAudioSource extends AudioSource {

    /**
     * Pauses the audio source.
     *
     * <p>Equivalent to calling {@code setPaused(true)}.
     *
     * @see #setPaused(boolean)
     */
    default void pause() {
        setPaused(true);
    }

    /**
     * Resumes the audio source.
     *
     * <p>Equivalent to calling {@code setPaused(false)}.
     *
     * @see #setPaused(boolean)
     */
    default void resume() {
        setPaused(false);
    }

    /**
     * Sets whether the audio source should be paused.
     *
     * <p>A paused audio source will completely stop.
     * This means, that after unpausing the audio source will continue at the same "position".
     *
     * @param paused Whether the audio source should be paused.
     */
    void setPaused(boolean paused);

    /**
     * Checks if the audio source is paused.
     *
     * @return Whether the audio source is paused or not.
     */
    boolean isPaused();

}
