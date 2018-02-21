package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerVoiceChannelChangeBitrateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
