package org.javacord.core.event.audio;

import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.event.audio.AudioSourceFinishedEvent;

/**
 * The implementation of {@link AudioSourceFinishedEvent}.
 */
public class AudioSourceFinishedEventImpl extends AudioSourceEventImpl implements AudioSourceFinishedEvent {

    /**
     * Creates a new audio source finished event.
     *
     * @param source The audio source of the event.
     * @param connection The audio connection of the event.
     */
    public AudioSourceFinishedEventImpl(AudioSource source, AudioConnection connection) {
        super(source, connection);
    }
}
