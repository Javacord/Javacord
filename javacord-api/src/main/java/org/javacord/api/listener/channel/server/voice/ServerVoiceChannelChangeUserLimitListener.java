package org.javacord.api.listener.channel.server.voice;

import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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
