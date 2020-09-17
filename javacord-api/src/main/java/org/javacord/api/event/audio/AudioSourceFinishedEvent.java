package org.javacord.api.event.audio;

import org.javacord.api.audio.AudioSource;

import java.util.Optional;

/**
 * An audio source finished event.
 */
public interface AudioSourceFinishedEvent extends AudioSourceEvent {

    /**
     * Gets the next source of the audio connection.
     *
     * <p>This method is equal to calling {@code getConnection().getCurrentAudioSource()}.
     *
     * @return The next source of the audio connection.
     */
    default Optional<AudioSource> getNextSource() {
        return getConnection().getCurrentAudioSource();
    }

}
