package org.javacord.api.listener.channel.server.voice;

import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server voice channel bitrate changes.
 */
@FunctionalInterface
public interface ServerVoiceChannelChangeBitrateListener extends ServerAttachableListener,
                                                              ServerVoiceChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server voice channel's bitrate changes.
     *
     * @param event The event.
     */
    void onServerVoiceChannelChangeBitrate(ServerVoiceChannelChangeBitrateEvent event);
}
