package org.javacord.api.listener.audio;

import org.javacord.api.event.audio.AudioSourceFinishedEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to finished audio sources.
 *
 * <p>It can be used to implement functionality like loops etc.
 */
@FunctionalInterface
public interface AudioSourceFinishedListener extends
        AudioSourceAttachableListener,
        AudioConnectionAttachableListener,
        GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time an audio connection finishes and was removed from the audio connection.
     *
     * @param event The event.
     */
    void onAudioSourceFinished(AudioSourceFinishedEvent event);

}
