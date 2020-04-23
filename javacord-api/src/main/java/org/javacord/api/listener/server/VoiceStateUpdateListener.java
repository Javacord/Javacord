package org.javacord.api.listener.server;

import org.javacord.api.event.server.VoiceStateUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListener;

/**
 * This listener listens to voice state updates for the current user.
 */
@FunctionalInterface
public interface VoiceStateUpdateListener extends ServerChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a voice state update packet is received for the current user.
     *
     * @param event The event.
     */
    void onVoiceStateUpdate(VoiceStateUpdateEvent event);

}
