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
     * @return Whether or not there is a frame available to be polled.
     */
    boolean hasNextFrame();

    /**
     * Creates a copy of the audio source which can be reused for another audio connection.
     *
     * @return A copy of the audio source.
     */
    AudioSource copy();
}
