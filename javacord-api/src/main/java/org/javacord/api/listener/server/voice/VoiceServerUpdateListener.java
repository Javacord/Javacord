package org.javacord.api.listener.server.voice;

import org.javacord.api.event.server.voice.VoiceServerUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to voice server updates.
 */
@FunctionalInterface
public interface VoiceServerUpdateListener extends ServerAttachableListener, GloballyAttachableListener,
                                                   ObjectAttachableListener {

    /**
     * This method is called every time a voice server update is received.
     *
     * @param event The event.
     */
    void onVoiceServerUpdate(VoiceServerUpdateEvent event);
}
