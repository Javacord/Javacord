package de.btobastian.javacord.listener.channel.server.voice;

import de.btobastian.javacord.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
