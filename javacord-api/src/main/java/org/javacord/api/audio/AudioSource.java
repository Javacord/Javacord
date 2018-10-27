package org.javacord.api.audio;

public interface AudioSource extends Cloneable {

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
     * Creates a copy of the audio source which can be reused for another audio connection.
     *
     * @return A copy of the audio source.
     */
    AudioSource copy();
}
