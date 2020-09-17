package org.javacord.api.listener.server;

import org.javacord.api.event.server.VoiceServerUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to voice server updates.
 */
@FunctionalInterface
public interface VoiceServerUpdateListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a voice server update packet is received.
     *
     * @param event the event.
     */
    void onVoiceServerUpdate(VoiceServerUpdateEvent event);

}
