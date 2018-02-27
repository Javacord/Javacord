package de.btobastian.javacord.listener.channel.server.voice;

import de.btobastian.javacord.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to server voice channel user limit changes.
 */
@FunctionalInterface
public interface ServerVoiceChannelChangeUserLimitListener extends ServerAttachableListener,
                                                              ServerVoiceChannelAttachableListener,
                                                              GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server voice channel's user limit changes.
     *
     * @param event The event.
     */
    void onServerVoiceChannelChangeUserLimit(ServerVoiceChannelChangeUserLimitEvent event);
}
