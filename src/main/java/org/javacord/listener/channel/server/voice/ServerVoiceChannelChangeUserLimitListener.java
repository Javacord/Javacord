package org.javacord.listener.channel.server.voice;

import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
